import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Graph extends StatefulWidget {
  const Graph({super.key});

  @override
  State<Graph> createState() => _GraphState();
}

class _GraphState extends State<Graph> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Column(
          children: [Text("Graph")],
        ),
      ),
    );
  }
}
