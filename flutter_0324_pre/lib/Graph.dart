import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_0324_pre/ShareList.dart';

class GraphScreen extends StatefulWidget {
  const GraphScreen({super.key});

  @override
  State<GraphScreen> createState() => _GraphState();
}

class _GraphState extends State<GraphScreen> {
  var data = ShareList().data;
  var maxValue = 0;
  List<int> daysList = [];

  @override
  void initState() {
    super.initState();
    daysList = List.filled(7, 0);
    data.forEach((item) => daysList[item.day] += item.time);
    maxValue = daysList.reduce((a, b) => max(a, b));
    setState(() {});
    print(maxValue);
    print(daysList);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Graph"),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          SizedBox(
            height: 400,
            width: double.infinity,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: dayOpt.asMap().entries.map((key) {
                return TweenAnimationBuilder(
                  builder: (context, value, widget) => Padding(
                    padding: EdgeInsets.only(
                        right: key.key == dayOpt.length ? 0 : 20),
                    child: LayoutBuilder(
                      builder: (context, size) => Column(
                        children: [
                          Container(
                            color: Colors.blueAccent,
                            height: 300,
                            width: 30,
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.end,
                              children: [
                                Container(
                                  width: 30,
                                  height: value.toDouble(),
                                  color: Colors.lightBlue,
                                ),
                              ],
                            ),
                          ),
                          Text(key.value)
                        ],
                      ),
                    ),
                  ),
                  tween: Tween(
                      begin: 0.0,
                      end: maxValue > 0
                          ? (daysList[key.key] / maxValue) * 300
                          : 0),
                  duration: Duration(seconds: 1),
                );
              }).toList(),
            ),
          )
        ],
      ),
    );
  }
}
