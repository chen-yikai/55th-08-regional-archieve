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
  var loading = false;
  var isDark = false;

  @override
  void initState() {
    fetcher();
    super.initState();
  }

  Future<void> fetcher() async {
    setState(() {
      data = [];
      loading = true;
    });
    final jsonString = await rootBundle.loadString('assets/data.json');
    final jsonData = jsonDecode(jsonString);
    setState(() {
      data = jsonData['result']['results'];
      loading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: isDark ? ThemeData.dark() : ThemeData.light(),
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: Text("台北捷運列車到站站名"),
          actions: [
            PopupMenuButton(
                itemBuilder: (context) => [
                      PopupMenuItem(
                        child: Text(isDark ? "Light Mode" : "Dark Mode"),
                        onTap: () {
                          setState(() {
                            isDark = !isDark;
                          });
                        },
                      )
                    ])
          ],
        ),
        body: Column(
          children: [
            Row(
              children: [
                Expanded(
                    child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: ElevatedButton(
                      onPressed: () {
                        setState(() {
                          data = [];
                        });
                      },
                      child: Text("重設")),
                )),
                Expanded(
                    child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: ElevatedButton(
                      onPressed: () {
                        fetcher();
                      },
                      child: Text("解析")),
                ))
              ],
            ),
            Expanded(
                child: ListView.builder(
                    itemCount: data.length,
                    itemBuilder: (context, index) {
                      final it = data[index];
                      return ListTile(
                          onTap: () => showDialog(
                              builder: (context) => AlertDialog(
                                    title: Text(
                                        "${it['Station']} -> ${it['Destination']}"),
                                    content: Text("更新時間"),
                                    actions: [
                                      TextButton(
                                          onPressed: () {
                                            Navigator.pop(context);
                                          },
                                          child: Text("確定"))
                                    ],
                                  ),
                              context: context),
                          title:
                              Text("${it['Station']} -> ${it['Destination']}"));
                    }))
          ],
        ),
      ),
    );
  }
}
