import 'package:flutter/material.dart';
import 'dart:math' as math; // For converting degrees to radians

void main() {
  runApp(AnimatedLine());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Rotate Line Example')),
        body: Center(
          child: CustomPaint(
            size: Size(200, 200), // Canvas size
            painter: LinePainter(),
          ),
        ),
      ),
    );
  }
}

class LinePainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = Colors.blue
      ..strokeWidth = 4.0
      ..style = PaintingStyle.stroke;

    // Define the line's start and end points
    Offset start = Offset(size.width / 2 - 50, size.height / 2); // Start point
    Offset end = Offset(size.width / 2 + 50, size.height / 2); // End point

    // Save the canvas state
    canvas.save();

    final center = Offset(size.width / 2, size.height / 2);
    canvas.translate(center.dx, center.dy);

    canvas.rotate(45 * math.pi / 180);

    canvas.drawLine(
      Offset(start.dx - center.dx, start.dy - center.dy),
      Offset(end.dx - center.dx, end.dy - center.dy),
      paint,
    );

    // Restore the canvas to its original state
    canvas.restore();
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

class AnimatedLine extends StatefulWidget {
  @override
  _AnimatedLineState createState() => _AnimatedLineState();
}

class _AnimatedLineState extends State<AnimatedLine>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: Duration(seconds: 2),
      vsync: this,
    )..repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return CustomPaint(
      size: Size(200, 200),
      painter: AnimatedLinePainter(_controller),
    );
  }
}

class AnimatedLinePainter extends CustomPainter {
  final Animation<double> animation;

  AnimatedLinePainter(this.animation) : super(repaint: animation);

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = Colors.blue
      ..strokeWidth = 4.0
      ..style = PaintingStyle.stroke;

    Offset center = Offset(size.width / 2, size.height / 2);
    Offset start = Offset(size.width / 2 - 50, size.height / 2);
    Offset end = Offset(size.width / 2 + 50, size.height / 2);

    canvas.save();
    canvas.translate(center.dx, center.dy);
    canvas.rotate(animation.value * 2 * math.pi);
    canvas.drawLine(
      Offset(start.dx - center.dx, start.dy - center.dy),
      Offset(end.dx - center.dx, end.dy - center.dy),
      paint,
    );
    canvas.restore();
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}