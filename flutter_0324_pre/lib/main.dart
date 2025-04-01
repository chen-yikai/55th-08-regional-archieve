import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_0324_pre/Drawer.dart';
import 'package:flutter_0324_pre/NewTodo.dart';
import 'package:flutter_0324_pre/ShareList.dart';

void main() {
  runApp(MaterialApp(debugShowCheckedModeBanner: false, home: Home()));
}

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  late Timer timer;
  var counting = false;
  var currentCount = 30;
  var originalCount = 30;
  var data = ShareList().data;

  @override
  void initState() {
    super.initState();
  }

  void playItem(second) {
    setState(() {
      currentCount = second;
      originalCount = second;
    });
  }

  void start() {
    if (!counting) {
      setState(() {
        counting = true;
      });
      timer = Timer.periodic(Duration(seconds: 1), (_timer) {
        if (currentCount > 0) {
          currentCount--;
        } else {
          _timer.cancel();
          counting = false;
        }
        setState(() {});
      });
    }
  }

  void pause() {
    timer.cancel();
    setState(() {
      counting = false;
    });
  }

  String timeFormater(int time) {
    final mm = time ~/ 60;
    final ss = time % 60;
    return "${mm.toString().padLeft(2, '0')}:${ss.toString().padLeft(2, '0')}";
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: RootDrawer(context),
      bottomSheet: DraggableScrollableSheet(
          expand: false,
          initialChildSize: 0.2,
          minChildSize: 0.1,
          maxChildSize: 1.0,
          builder: (context, controller) {
            return SingleChildScrollView(
              controller: controller,
              child: Container(
                width: double.infinity,
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Container(
                        width: 100,
                        height: 7,
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(20),
                          color: Colors.grey,
                        ),
                      ),
                    ),
                    const Row(
                      children: [
                        Text("My Todo"),
                      ],
                    ),
                    Container(
                        height: 600,
                        child: ReorderableListView.builder(
                          itemCount: data.length,
                          itemBuilder: (context, index) {
                            final it = data[index];
                            return Dismissible(
                              key: ValueKey(it.id),
                              onDismissed: (dir) {
                                setState(() {
                                  data.removeAt(index);
                                });
                              },
                              background: Container(
                                color: Colors.redAccent,
                                child: Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Icon(Icons.delete),
                                    )
                                  ],
                                ),
                              ),
                              child: ListTile(
                                title: Text(it.name),
                                subtitle: Text(typeOpt[it.type]),
                                leading: IconButton(
                                    onPressed: () {
                                      playItem(it.time);
                                    },
                                    icon: Icon(Icons.play_arrow)),
                                trailing: Text(timeFormater(it.time) +
                                    '\n' +
                                    dayOpt[it.day]),
                              ),
                            );
                          },
                          onReorder: (int oldIndex, int newIndex) {
                            if (newIndex > oldIndex) newIndex--;
                            final temp = data[oldIndex];
                            data.removeAt(oldIndex);
                            data.insert(newIndex, temp);
                            setState(() {});
                          },
                        ))
                  ],
                ),
              ),
            );
          }),
      appBar: AppBar(
        title: Text("Tomato Bo"),
        actions: [
          IconButton(
              onPressed: () => NewTodo(context, () {
                    setState(() {});
                  }),
              icon: Icon(Icons.add))
        ],
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 300,
              height: 300,
              child: Stack(
                children: [
                  TweenAnimationBuilder(
                    tween: Tween(begin: 1.0, end: currentCount / originalCount),
                    duration: Duration(seconds: 1),
                    builder: (context, value, widget) {
                      return Container(
                        width: 300,
                        height: 300,
                        child: CircularProgressIndicator(
                          strokeWidth: 15,
                          value: value,
                        ),
                      );
                    },
                  ),
                  Align(
                    alignment: Alignment.center,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          timeFormater(currentCount),
                          style: TextStyle(
                              fontSize: 40, fontWeight: FontWeight.bold),
                        ),
                        IconButton(
                            onPressed: () {
                              if (counting) {
                                pause();
                              } else {
                                start();
                              }
                            },
                            icon:
                                Icon(counting ? Icons.pause : Icons.play_arrow))
                      ],
                    ),
                  )
                ],
              ),
            )
          ],
        ),
      ),
    );
  }
}
