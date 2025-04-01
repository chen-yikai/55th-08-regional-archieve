import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MaterialApp(home: MyApp()));
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var data = [];
  var loading = false;

  @override
  void initState() {
    super.initState();
    fetcher();
  }

  Future<void> fetcher() async {
    setState(() {
      loading = true;
      data = [];
    });
    await Future.delayed(Duration(seconds: 1));
    try {
      final jsonString = await rootBundle.loadString("assets/data.json");
      final jsonData = jsonDecode(jsonString);
      setState(() {
        data = jsonData['result']['results'];
        loading = false;
      });
    } catch (e) {}
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("台北捷運列車到站站名"),
        actions: [
          IconButton(onPressed: () {}, icon: Icon(Icons.more_vert_outlined))
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(10),
        child: Column(
          children: [
            Row(
              children: [
                Text(loading
                    ? "載入中..."
                    : data.isEmpty
                        ? "無資料"
                        : "載入成功"),
              ],
            ),
            Row(
              children: [
                Expanded(
                    child: ElevatedButton(
                        onPressed: () {
                          setState(() {
                            data = [];
                          });
                        },
                        child: Text("重設"))),
                SizedBox(width: 10),
                Expanded(
                    child: ElevatedButton(
                        onPressed: () {
                          fetcher();
                        },
                        child: Text("解析")))
              ],
            ),
            Expanded(
                child: ReorderableListView.builder(
              itemCount: data.length,
              itemBuilder: (context, index) {
                final it = data[index];
                return ListTile(
                  key: ValueKey(it['_id']),
                  title: Text(it["Station"] + "->" + it['Destination']),
                  onTap: () {
                    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                        content: Text("你選擇的是 " +
                            it["Station"] +
                            "->" +
                            it['Destination'])));
                  },
                );
              },
              onReorder: (int oldIndex, int newIndex) {
                setState(() {
                  if (newIndex > oldIndex) {
                    newIndex = newIndex - 1;
                  }
                  final temp = data.removeAt(oldIndex);
                  data.insert(newIndex, temp);
                });
              },
            ))
          ],
        ),
      ),
    );
  }
}
