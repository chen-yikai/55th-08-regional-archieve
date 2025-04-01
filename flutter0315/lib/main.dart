import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

enum schema {
  id,
  line,
  destination,
  status,
  arrival_time,
  departure,
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var data = [];
  var error = false;
  var showSearch = false;
  var loading = true;
  var searchController = TextEditingController();
  var currentTime = 0;
  var currentTheme = ThemeMode.system;
  var sortingMethod = 0;
  var reverse = false;

  @override
  void initState() {
    super.initState();
    fetch();
  }

  void reversing() {
    setState(() {
      data = data.reversed.toList();
    });
  }

  void sorting(String type) {
    setState(() {
      if (sortingMethod == 0) {
        data.sort((a, b) {
          final aId = a[type] ?? 0;
          final bId = b[type] ?? 0;
          return reverse ? aId.compareTo(bId) : bId.compareTo(bId);
        });
      }
    });
  }

  Future<void> fetch() async {
    setState(() {
      loading = true;
      data = [];
      error = false;
    });
    try {
      final client = HttpClient();
      final url = Uri.parse("https://skills-flutter-test-api.eliaschen.dev/");
      final request = await client.getUrl(url);
      final response = await request.close();
      if (response.statusCode == 200) {
        final jsonString = await response.transform(utf8.decoder).join();
        final jsonData = jsonDecode(jsonString);
        setState(() {
          currentTime = DateTime.parse(jsonData[0]['current_time'])
              .millisecondsSinceEpoch;
          data = jsonData[0]['schedule'];
        });
      } else {
        throw Exception("error");
      }
    } catch (e) {
      setState(() {
        error = true;
      });
    } finally {
      setState(() {
        loading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      themeMode: currentTheme,
      darkTheme: ThemeData(brightness: Brightness.dark),
      theme: ThemeData(brightness: Brightness.light),
      home: Scaffold(
        appBar: AppBar(
          title: Text("捷運即時資訊"),
          actions: [
            IconButton(
                onPressed: () {
                  fetch();
                },
                icon: Icon(Icons.refresh)),
            IconButton(
                onPressed: () {
                  setState(() {
                    data = [];
                  });
                },
                icon: Icon(Icons.delete)),
            IconButton(
                onPressed: () {
                  setState(() {
                    showSearch = !showSearch;
                  });
                },
                icon: Icon(showSearch ? Icons.search_off : Icons.search)),
            PopupMenuButton(itemBuilder: (context) {
              return [
                PopupMenuItem(
                  child: Text("System"),
                  onTap: () {
                    setState(() {
                      currentTheme = ThemeMode.system;
                    });
                  },
                ),
                PopupMenuItem(
                  child: Text("Light"),
                  onTap: () {
                    setState(() {
                      currentTheme = ThemeMode.light;
                    });
                  },
                ),
                PopupMenuItem(
                  child: Text("Dark"),
                  onTap: () {
                    setState(() {
                      currentTheme = ThemeMode.dark;
                    });
                  },
                )
              ];
            })
          ],
        ),
        body: Column(
          children: [
            showSearch
                ? Padding(
                    padding: const EdgeInsets.all(10),
                    child: Row(
                      children: [
                        Expanded(
                          child: SearchBar(
                            controller: searchController,
                            onChanged: (value) {
                              setState(() {});
                            },
                          ),
                        ),
                        IconButton(
                            onPressed: () {
                              setState(() {
                                reverse = !reverse;
                              });
                              reversing();
                            },
                            icon: reverse
                                ? Icon(Icons.arrow_downward)
                                : Icon(Icons.arrow_upward)),
                        PopupMenuButton(
                          itemBuilder: (context) {
                            return [
                              PopupMenuItem(
                                child: Text("id"),
                                onTap: () {
                                  sorting("id");
                                },
                              ),
                              PopupMenuItem(
                                child: Text("arrival_time"),
                                onTap: () {
                                  sorting("arrival_time");
                                },
                              ),
                            ];
                          },
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Icon(Icons.list),
                          ),
                        ),
                      ],
                    ),
                  )
                : SizedBox(),
            !error
                ? !loading
                    ? data.isNotEmpty
                        ? Expanded(
                            child: ListView.builder(
                                itemCount: data.length,
                                itemBuilder: (context, index) {
                                  final it = data[index];
                                  final thisTime = DateTime.parse(
                                          it[schema.arrival_time.name])
                                      .millisecondsSinceEpoch;
                                  final rest = thisTime - currentTime;

                                  return it[schema.line.name]
                                              .toString()
                                              .contains(
                                                  searchController.text) ||
                                          it[schema.destination.name]
                                              .toString()
                                              .contains(searchController.text)
                                      ? Dismissible(
                                          key: ValueKey(it[schema.id.name]),
                                          onDismissed: (a) {
                                            setState(() {
                                              data.removeAt(index);
                                            });
                                          },
                                          background: Container(
                                            color: Colors.red,
                                          ),
                                          child: ListTile(
                                              onTap: () {
                                                showDialog(
                                                    context: context,
                                                    builder: (context) =>
                                                        Dialog(
                                                          child: Padding(
                                                            padding:
                                                                const EdgeInsets
                                                                    .all(20),
                                                            child: Text(
                                                                "${it[schema.line.name]}\n${it[schema.departure.name]} -> ${it[schema.destination.name]}\n${it[schema.arrival_time.name]}"),
                                                          ),
                                                        ));
                                              },
                                              title: Text(
                                                  "${it[schema.destination.name]}"),
                                              subtitle:
                                                  Text(it[schema.line.name]),
                                              trailing: Text(it[
                                                          schema.status.name] ==
                                                      "進站中"
                                                  ? "進站中"
                                                  : DateTime.fromMillisecondsSinceEpoch(
                                                              rest)
                                                          .minute
                                                          .toString() +
                                                      "分鐘")),
                                        )
                                      : SizedBox();
                                }))
                        : Expanded(
                            child: Center(
                            child: Text("No Data"),
                          ))
                    : const Expanded(
                        child: Center(
                        child: CircularProgressIndicator(),
                      ))
                : const Expanded(
                    child: Center(
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: [Icon(Icons.error), Text("Error")],
                      ),
                    ),
                  ),
          ],
        ),
      ),
    );
  }
}
