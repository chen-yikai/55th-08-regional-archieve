import 'dart:ui' as ui;
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: SafeArea(
          child: Column(
            children: [
              CustomPaint(
                size: Size(200, 200),
                painter: Artist(),
              )
            ],
          ),
        ),
      ),
    );
  }
}

class Artist extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = Colors.red
      ..strokeWidth = 10
      ..style = PaintingStyle.stroke;
    final text = ui.TextStyle(
      color: Colors.black,
      fontSize: 30,
    );
    final textBuilder = ui.ParagraphBuilder(
        ui.ParagraphStyle(textDirection: ui.TextDirection.ltr))
      ..pushStyle(text)
      ..addText("hello canvas!");
    final textParargraph = textBuilder.build()
      ..layout(ui.ParagraphConstraints(width: 200));
    canvas.drawParagraph(textParargraph, ui.Offset(0, 0));
    // canvas.drawRect(const Rect.fromLTWH(0, 0, 50, 100), paint);
    // canvas.drawCircle(const Offset(50, 50), 50, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}
