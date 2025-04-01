import 'package:flutter/material.dart';

Widget Dragger() {
  return Column(
    children: [
      Padding(
        padding: EdgeInsets.only(top: 10),
        child: Container(
          decoration: BoxDecoration(
              color: Colors.grey, borderRadius: BorderRadius.circular(30)),
          width: 100,
          height: 6,
        ),
      ),
      SizedBox(height: 5),
    ],
  );
}

String timeFormatter(int seconds) {
  final mm = (seconds ~/ 60).toString().padLeft(2, "0");
  final ss = (seconds).toString().padLeft(2, "0");
  return "$mm:$ss";
}
