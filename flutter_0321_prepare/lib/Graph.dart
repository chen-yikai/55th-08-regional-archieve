import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_0321_prepare/Task.dart';
import 'package:flutter_0321_prepare/main.dart';

class Graph extends StatefulWidget {
  const Graph({super.key});

  @override
  State<Graph> createState() => _GraphState();
}

class _GraphState extends State<Graph> {
  late int maxTime;

  List<ListSchema> data = [
    ListSchema("Feed my kitty", 20, "rest", 0),
    ListSchema("Fed", 10, "rest", 1),
    ListSchema("Fed", 10, "rest", 2),
    ListSchema("Fed", 10, "rest", 3),
    ListSchema("Fed", 10, "rest", 5),
    ListSchema("Fed", 10, "rest", 4),
    ListSchema("Fed", 10, "rest", 1),
    ListSchema("Fed", 40, "rest", 6),
  ];
  List<int> daysCount = [];
  List<String> daysChar = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

  @override
  void initState() {
    super.initState();

    daysCount = List.filled(7, 0);
    print(daysCount);

    for (var item in data) {
      if (item.day >= 0 && item.day < 7) {
        daysCount[item.day] += item.seconds;
      }
    }

    maxTime = daysCount.reduce((a, b) => max(a, b));
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Graph"),
      ),
      drawer: Drawer(
        child: ListView(
          children: [
            ListTile(
              title: Text("Home",),
              onTap: (){
                Navigator.push(context, MaterialPageRoute(builder: (context)=>App()));
              },
            )
          ],
        ),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(
            child: Container(
              width: 400,
              height: 400,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: daysChar.asMap().entries.map((entry) {
                  return Padding(
                    padding: EdgeInsets.only(right: entry.key == 6 ? 0 : 30),
                    child: Column(
                      children: [
                        TweenAnimationBuilder(
                          tween: Tween(
                              begin: 0.0,
                              end: maxTime > 0
                                  ? (daysCount[entry.key] / maxTime) * 300
                                  : 0),
                          duration: const Duration(seconds: 1),
                          builder: (context, value, widget) => Container(
                            width: 20,
                            height: 300,
                            color: Colors.blueAccent,
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.end,
                              children: [
                                Container(
                                  height: value.toDouble(),
                                  color: Colors.lightBlueAccent,
                                )
                              ],
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top: 10),
                          child: Text(
                            entry.value,
                            style: const TextStyle(
                                fontWeight: FontWeight.bold, fontSize: 15),
                          ),
                        )
                      ],
                    ),
                  );
                }).toList(),
              ),
            ),
          )
        ],
      ),
    );
  }
}
