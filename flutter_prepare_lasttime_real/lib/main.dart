import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
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
    try {
      setState(() {
        data = [];
        loading = true;
      });
      await Future.delayed(Duration(seconds: 1));
      final jsonString = await rootBundle.loadString("assets/wow.json");
      final jsonData = jsonDecode(jsonString);
      setState(() {
        data = jsonData['result']['results'];
        loading = false;
      });
    } catch (e) {}
  }

  void sorting() {
    setState(() {
      data.sort((a, b) => DateTime.parse(b['UpdateTime'])
          .millisecondsSinceEpoch
          .compareTo(DateTime.parse(a['UpdateTime']).millisecondsSinceEpoch));
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: Text("台北捷運到站站名"),
          actions: [IconButton(onPressed: sorting, icon: Icon(Icons.sort))],
        ),
        body: Padding(
          padding: const EdgeInsets.all(10),
          child: Column(
            children: [
              Row(
                children: [
                  Text(
                    loading
                        ? "載入中..."
                        : data.isEmpty
                            ? "無資料"
                            : "解析成功",
                    style: TextStyle(fontSize: 20),
                  )
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
                        child: Text("重設")),
                  ),
                  SizedBox(width: 10),
                  Expanded(
                    child: ElevatedButton(
                        onPressed: () {
                          fetcher();
                        },
                        child: Text("解析")),
                  ),
                ],
              ),
              Expanded(
                child: ListView.builder(
                  itemCount: data.length,
                  itemBuilder: (context, index) {
                    final it = data[index];
                    return ListTile(
                      onTap: () {
                        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                            content: Text("你選擇的是 " +
                                it['Station'] +
                                '->' +
                                it['Destination'])));
                      },
                      title: Text(it['Station'] + '->' + it['Destination']),
                    );
                  },
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
