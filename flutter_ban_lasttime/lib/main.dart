import 'dart:convert';

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
    fetcher();
    super.initState();
  }

  Future<void> fetcher() async {
    setState(() {
      loading = true;
    });
    await Future.delayed(Duration(seconds: 1));
    try {
      final jsonString = await rootBundle.loadString("assets/data.json");
      final jsonData = jsonDecode(jsonString);
      setState(() {
        data = jsonData['result']['results'];
        loading = false;
      });
    } catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: Text("台北捷運列車到站站名"),
        ),
        body: Padding(
          padding: const EdgeInsets.all(10),
          child: Column(
            children: [
              Row(
                children: [
                  Text(
                    loading
                        ? '解析中，請稍後'
                        : data.isEmpty
                            ? "無資料"
                            : "解析成功",
                    style: TextStyle(fontSize: 25),
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
                          title: Text(it['Station'] + "->" + it['Destination']),
                        );
                      }))
            ],
          ),
        ),
      ),
    );
  }
}
