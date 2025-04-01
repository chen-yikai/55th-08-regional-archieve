import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_0321_prepare/Graph.dart';
import 'package:flutter_0321_prepare/home/HomeAppBar.dart';

void main() {
  SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
    statusBarColor: Colors.transparent,
    systemNavigationBarColor: Colors.transparent,
  ));
  runApp(const MaterialApp(
    home: Graph(),
    debugShowCheckedModeBanner: false,
  ));
}

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  var sec = 20;
  var og_sec = 20;
  late Timer timer;
  var isCounting = false;

  void start() {
    if (!isCounting) {
      setState(() {
        isCounting = true;
      });
      timer = Timer.periodic(const Duration(seconds: 1), (time) {
        setState(() {
          if (sec > 0) {
            sec--;
          } else {
            time.cancel();
            isCounting = false;
          }
        });
      });
    }
  }

  void pause() {
    setState(() {
      timer.cancel();
      isCounting = false;
    });
  }

  String timeFormatter(int time) {
    final mm = (time ~/ 60).toString().padLeft(2, '0');
    final ss = (time % 60).toString().padLeft(2, '0');
    return "$mm:$ss";
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      bottomSheet: DraggableScrollableSheet(
        expand: false,
        initialChildSize: 0.2,
        minChildSize: 0.2,
        maxChildSize: 0.9,
        builder: (context, controller) {
          return SingleChildScrollView(
              controller: controller,
              child: Container(
                width: double.infinity,
                child: Column(
                  children: [
                    Padding(
                      padding: EdgeInsets.symmetric(horizontal: 10),
                      child: Container(
                        width: 100,
                        height: 7,
                        decoration: BoxDecoration(
                            color: Colors.grey,
                            borderRadius: BorderRadius.circular(10)),
                      ),
                    )
                  ],
                ),
              ));
        },
      ),
      drawer: Drawer(
        child: ListView(
          children: [
            DrawerHeader(child: Text("Timer App")),
            ListTile(
              title: Text("Graph"),
              onTap: () {
                Navigator.push(
                    context, MaterialPageRoute(builder: (context) => Graph()));
              },
            )
          ],
        ),
      ),
      appBar: HomeAppBar(context),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(
            child: Container(
              height: 300,
              width: 300,
              child: Stack(
                alignment: Alignment.center,
                children: [
                  SizedBox(
                      width: 300,
                      height: 300,
                      child: TweenAnimationBuilder(
                        tween: Tween(
                            begin: 1.0, end: sec > 0 ? sec / og_sec : 1.0),
                        duration: const Duration(seconds: 1),
                        builder: (context, value, child) =>
                            CircularProgressIndicator(
                          strokeWidth: 15,
                          value: value.toDouble(),
                        ),
                      )),
                  Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        timeFormatter(sec),
                        style: TextStyle(
                            fontSize: 50, fontWeight: FontWeight.bold),
                      ),
                      IconButton(
                          onPressed: isCounting ? pause : start,
                          icon: Icon(
                            isCounting ? Icons.pause : Icons.play_arrow,
                            size: 60,
                            color: Colors.black,
                          ))
                    ],
                  ),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }
}

class homeBar extends StatefulWidget {
  const homeBar({super.key});

  @override
  State<homeBar> createState() => _homeBarState();
}

class _homeBarState extends State<homeBar> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
