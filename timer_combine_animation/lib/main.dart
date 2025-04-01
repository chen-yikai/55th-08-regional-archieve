import 'dart:async';

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
  var currentTime = 60;
  var originalTime = 60;
  late Timer timer;
  var timerStarted = false;
  late AnimationController _animationController;
  late Animation<double> _animation;

  void start() {
    if (!timerStarted) {
      setState(() {
        timerStarted = true;
      });
      timer = Timer.periodic(const Duration(seconds: 1), (time) {
        setState(() {
          if (currentTime > 0) {
            currentTime--;
          } else {
            timer.cancel();
            timerStarted = false;
          }
        });
      });
    } else {
      return;
    }
  }

  void pause() {
    timer.cancel();
    setState(() {
      timerStarted = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        bottomSheet: DraggableScrollableSheet(
          expand: false,
          initialChildSize: 0.15,
          minChildSize: 0.15,
          maxChildSize: 1.0,
          builder: (context, scroller) {
            return Container(
              width: double.infinity,
              decoration: BoxDecoration(color: Colors.white),
              child: SingleChildScrollView(
                controller: scroller,
                child: Column(
                  children: [Text("hello")],
                ),
              ),
            );
          },
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {},
          child: Icon(Icons.add),
        ),
        drawer: Drawer(
          child: Column(
            children: [Text("hello")],
          ),
        ),
        body: SafeArea(
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Stack(
                  alignment: Alignment.center,
                  children: [
                    SizedBox(
                      width: 300,
                      height: 300,
                      child: CircularProgressIndicator(
                        value: currentTime / originalTime,
                        strokeWidth: 10,
                      ),
                    ),
                    Column(
                      children: [
                        Text(
                          currentTime.toString(),
                          style: TextStyle(fontSize: 40),
                        ),
                        IconButton(
                            onPressed: () {
                              if (timerStarted) {
                                pause();
                              } else {
                                start();
                              }
                            },
                            icon: Icon(
                                timerStarted ? Icons.pause : Icons.play_arrow,
                                size: 50))
                      ],
                    ),
                  ],
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
