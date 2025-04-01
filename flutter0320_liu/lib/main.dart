import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(App());
}

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  var data = [];

  @override
  void initState() {
    fetcher();
    super.initState();
  }

  Future<void> fetcher() async {
    final jsonString = await rootBundle.loadString('assets/data.json');
    final jsonData = jsonDecode(jsonString);
    setState(() {
      data = jsonData['result']['results'];
    });
  }

  void sorting(String type) {
    setState(() {
      data.sort((a, b) => a[type].toString().compareTo(b[type].toString()));
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: Text("Json parsing"),
          actions: [
            PopupMenuButton(itemBuilder: (context) {
              return [
                PopupMenuItem(
                  child: Text("Id"),
                  onTap: () => sorting("_id"),
                ),
                PopupMenuItem(
                  child: Text("Update Time"),
                  onTap: () => sorting("UpdateTime"),
                ),
              ];
            })
          ],
        ),
        body: Column(
          children: [
            Expanded(
              child: ListView.builder(
                itemCount: data.length,
                itemBuilder: (context, index) {
                  final it = data[index];
                  return Dismissible(
                    key: ValueKey(it['_id'].toString()),
                    onDismissed: (dir) {
                      setState(() {
                        data.removeAt(index);
                      });
                    },
                    child: ListTile(
                      onTap: () {
                        showDialog(
                            context: context,
                            builder: (context) {
                              return Dialog(
                                child: Padding(
                                  padding: const EdgeInsets.all(20),
                                  child: Text(
                                      "${it['Station']} -> ${it['Destination']}\n${it['UpdateTime']}"),
                                ),
                              );
                            });
                      },
                      title: Text("${it['Station']} -> ${it['Destination']}"),
                    ),
                  );
                },
              ),
            )
          ],
        ),
      ),
    );
  }
}
