import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var data = [];
  var error = false;
  var loading = false;
  var showSearch = false;
  final searchController = TextEditingController();
  late var currentTime;

  @override
  void initState() {
    fetcher();
    super.initState();
  }

  Future<void> fetcher() async {
    setState(() {
      data = [];
      error = false;
      loading = true;
    });
    final client = HttpClient();
    try {
      final request = await client
          .getUrl(Uri.parse("https://skills-flutter-test-api.eliaschen.dev/"));
      final response = await request.close();
      if (response.statusCode == 200) {
        final apiString = await response.transform(utf8.decoder).join();
        final jsonString = jsonDecode(apiString);
        setState(() {
          currentTime = DateTime.parse(jsonString[0]['current_time'])
              .millisecondsSinceEpoch;
          data = jsonString[0]['schedule'];
        });
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
      home: Scaffold(
        appBar: AppBar(
          title: Text(error
              ? "Error"
              : loading
                  ? "Loading..."
                  : "忠孝新生時刻表"),
          actions: [
            IconButton(
                onPressed: () {
                  setState(() {
                    showSearch = !showSearch;
                  });
                },
                icon: Icon(showSearch ? Icons.search_off : Icons.search)),
            IconButton(
                onPressed: () {
                  setState(() {
                    data = [];
                  });
                },
                icon: Icon(Icons.delete)),
            IconButton(
                onPressed: () {
                  fetcher();
                },
                icon: Icon(Icons.refresh))
          ],
        ),
        body: Column(
          children: [
            if (showSearch)
              Padding(
                padding: const EdgeInsets.all(10),
                child: SearchBar(
                  controller: searchController,
                  onChanged: (a) {
                    setState(() {});
                  },
                  trailing: [
                    if (searchController.text.isNotEmpty)
                      IconButton(onPressed: () {}, icon: Icon(Icons.cancel))
                  ],
                ),
              ),
            Expanded(
                child: !error
                    ? !loading
                        ? ListView.builder(
                            itemCount: data.length,
                            itemBuilder: (context, index) {
                              final it = data[index];
                              final thisTime =
                                  DateTime.parse(it['arrival_time'])
                                      .millisecondsSinceEpoch;
                              final restTime =
                                  DateTime.fromMillisecondsSinceEpoch(
                                      (thisTime - currentTime).toInt());

                              return it['line']
                                          .toString()
                                          .contains(searchController.text) ||
                                      it['destination']
                                          .toString()
                                          .contains(searchController.text)
                                  ? Column(
                                      children: [
                                        ListTile(
                                          onTap: () {
                                            showDialog(
                                                context: context,
                                                builder: (context) {
                                                  return Dialog(
                                                      child: Padding(
                                                    padding:
                                                        const EdgeInsets.all(
                                                            20),
                                                    child: Text(
                                                        "${it['departure']} -> ${it['destination']}\n${it['line']}\n列出目前在${it['status'] == "進站中" ? "忠孝新生" : it['status']}"),
                                                  ));
                                                });
                                          },
                                          title: Text("${it['destination']}"),
                                          subtitle: Text(it['line']),
                                          trailing: Text(restTime.minute != 0
                                              ? "${restTime.minute.toString()}分鐘"
                                              : "進站中"),
                                        ),
                                        Divider()
                                      ],
                                    )
                                  : SizedBox();
                            },
                          )
                        : const Center(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                CircularProgressIndicator(),
                              ],
                            ),
                          )
                    : const Center(
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [Icon(Icons.error), Text("Error")],
                        ),
                      ))
          ],
        ),
      ),
    );
  }
}