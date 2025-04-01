import 'dart:async';
import 'package:flutter/material.dart';

import 'package:flutter_0317_clock/graph.dart';

void main() {
  runApp(const MaterialApp(debugShowCheckedModeBanner: false, home: MyApp()));
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> with SingleTickerProviderStateMixin {
  List<Map<String, dynamic>> tasks = [
    {"id": "1", "name": "Feed the cat with liu", "time": 10, "type": 0},
    {"id": "2", "name": "Do the homework with lee", "time": 20, "type": 1},
    {"id": "3", "name": "Coding with sofia", "time": 5, "type": 1},
    {"id": "4", "name": "Cook dinner with eugene", "time": 10, "type": 1},
  ];
  bool isCounting = false;
  int currentTime = 0;
  int originalTime = 0;
  late Timer timer;
  int currentTask = 0;

  var workNameController = TextEditingController();
  var workMinuteController = TextEditingController();
  var workSecondController = TextEditingController();
  var workType = TextEditingController();

  late AnimationController _animationController;
  late Animation<double> _progressAnimation;

  String timerFormatter(int seconds) {
    final mm = (seconds ~/ 60).toString().padLeft(2, '0');
    final ss = (seconds % 60).toString().padLeft(2, '0');
    return "$mm:$ss";
  }

  @override
  void initState() {
    super.initState();
    initTask();
  }

  void start() {
    if (!isCounting) {
      setState(() {
        isCounting = true;
      });
      timer = Timer.periodic(const Duration(seconds: 1), (_timer) {
        setState(() {
          if (currentTime > 0) {
            currentTime--;
          } else if (currentTask < tasks.length - 1) {
            currentTask++;
            initTask();
          } else {
            _timer.cancel();
            isCounting = false;
          }
        });
      });
    }
  }

  void initTask() {
    setState(() {
      currentTime = tasks[currentTask]["time"];
      originalTime = tasks[currentTask]["time"];
    });
  }

  void pause() {
    if (isCounting) {
      timer.cancel();
      setState(() {
        isCounting = false;
      });
    }
  }

  String workTypeTransform(int type) {
    return type == 0 ? "Rest" : "Work";
  }

  Color workTypeColor(int type) {
    return type == 0 ? Color(0xffC89BFF) : Color(0xffFF6F83);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      bottomSheet: DraggableScrollableSheet(
        expand: false,
        initialChildSize: 0.2,
        minChildSize: 0.2,
        maxChildSize: 1,
        builder: (context, scroller) {
          return SingleChildScrollView(
            controller: scroller,
            child: Container(
              width: double.infinity,
              height: 500,
              child: Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(top: 10),
                    child: Opacity(
                      opacity: 0.3,
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.grey,
                          borderRadius: BorderRadius.circular(20),
                        ),
                        width: 100,
                        height: 7,
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 15),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          "Your Work",
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 30,
                          ),
                        ),
                        Text(
                          "${tasks.length} in total",
                          style: const TextStyle(color: Colors.grey),
                        ),
                      ],
                    ),
                  ),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 10),
                      child: ReorderableListView.builder(
                        itemCount: tasks.length,
                        itemBuilder: (context, index) {
                          final it = tasks[index];
                          return Dismissible(
                            key: ValueKey(it["id"]),
                            onDismissed: (direction) {
                              setState(() {
                                tasks.removeAt(index);
                                if (currentTask >= tasks.length) {
                                  currentTask = tasks.length - 1;
                                }
                                if (tasks.isNotEmpty) {
                                  initTask();
                                } else {
                                  currentTime = 0;
                                  originalTime = 0;
                                }
                              });
                            },
                            background: Container(
                              color: Colors.red,
                              child: const Padding(
                                padding: EdgeInsets.symmetric(horizontal: 10),
                                child: Row(
                                  children: [
                                    Icon(Icons.delete),
                                  ],
                                ),
                              ),
                            ),
                            secondaryBackground: Container(
                              color: Colors.red,
                              child: const Padding(
                                padding: EdgeInsets.symmetric(horizontal: 10),
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.end,
                                  children: [
                                    Icon(Icons.delete),
                                  ],
                                ),
                              ),
                            ),
                            child: ListTile(
                              title: Text(it["name"].toString()),
                              subtitle: Row(
                                children: [
                                  Container(
                                      decoration: BoxDecoration(
                                          borderRadius:
                                              BorderRadius.circular(20),
                                          color: workTypeColor(it['type'])),
                                      child: Padding(
                                        padding: const EdgeInsets.symmetric(
                                            horizontal: 10, vertical: 2),
                                        child:
                                            Text(workTypeTransform(it["type"])),
                                      )),
                                ],
                              ),
                              trailing: Text(
                                timerFormatter(it["time"]),
                                style: const TextStyle(
                                    fontSize: 20, color: Colors.black),
                              ),
                            ),
                          );
                        },
                        onReorder: (int oldIndex, int newIndex) {
                          setState(() {
                            if (newIndex > oldIndex) {
                              newIndex -= 1;
                            }
                            final item = tasks.removeAt(oldIndex);
                            tasks.insert(newIndex, item);
                          });
                        },
                      ),
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
      appBar: AppBar(
        title: const Text("Timer"),
        actions: [
          IconButton(
            onPressed: () {
              showModalBottomSheet(
                context: context,
                builder: (context) {
                  return Container(
                    width: double.infinity,
                    child: Column(
                      children: [
                        Padding(
                          padding: const EdgeInsets.only(top: 10),
                          child: Opacity(
                            opacity: 0.3,
                            child: Container(
                              decoration: BoxDecoration(
                                color: Colors.grey,
                                borderRadius: BorderRadius.circular(20),
                              ),
                              width: 100,
                              height: 7,
                            ),
                          ),
                        ),
                        const SizedBox(height: 20),
                        const Padding(
                          padding: EdgeInsets.symmetric(horizontal: 20),
                          child: Row(
                            children: [
                              Text(
                                "New Work",
                                style: TextStyle(
                                  fontWeight: FontWeight.bold,
                                  fontSize: 30,
                                ),
                              ),
                            ],
                          ),
                        ),
                        SizedBox(height: 20),
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 20),
                          child: Column(
                            children: [
                              SizedBox(
                                width: double.infinity,
                                child: TextField(
                                  controller: workNameController,
                                  decoration: const InputDecoration(
                                      hintText: "Name",
                                      border: OutlineInputBorder()),
                                ),
                              ),
                              const SizedBox(height: 20),
                              Row(
                                children: [
                                  Expanded(
                                    child: TextField(
                                      controller: workMinuteController,
                                      decoration: const InputDecoration(
                                          border: OutlineInputBorder(),
                                          hintText: "Minutes"),
                                      keyboardType: TextInputType.number,
                                    ),
                                  ),
                                  const SizedBox(width: 20),
                                  Expanded(
                                    child: TextField(
                                      controller: workSecondController,
                                      decoration: const InputDecoration(
                                          border: OutlineInputBorder(),
                                          hintText: "Seconds"),
                                      keyboardType: TextInputType.number,
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 20),
                              Row(
                                children: [
                                  DropdownMenu(
                                      label: Text("Type"),
                                      helperText: "Chose the work type",
                                      width: 200,
                                      initialSelection: 0,
                                      controller: workType,
                                      dropdownMenuEntries: [
                                        DropdownMenuEntry(
                                            value: 0, label: "Rest"),
                                        DropdownMenuEntry(
                                            value: 1, label: "Work"),
                                      ]),
                                ],
                              ),
                              const SizedBox(height: 30),
                              SizedBox(
                                height: 40,
                                width: double.infinity,
                                child: ElevatedButton(
                                  onPressed: () {
                                    final timeSec = (int.tryParse(
                                                    workMinuteController
                                                        .text) ??
                                                0) *
                                            60 +
                                        (int.tryParse(
                                                workSecondController.text) ??
                                            0);
                                    setState(() {
                                      tasks.add({
                                        "id": DateTime.now()
                                            .millisecondsSinceEpoch
                                            .toString(),
                                        "name": workNameController.text,
                                        "time": timeSec,
                                        "type":
                                            int.tryParse(workType.text) ?? 0,
                                      });
                                      workType.clear();
                                      workNameController.clear();
                                      workSecondController.clear();
                                      workMinuteController.clear();
                                    });
                                    Navigator.pop(context);
                                  },
                                  style: ElevatedButton.styleFrom(
                                      backgroundColor: Colors.blue,
                                      foregroundColor: Colors.white),
                                  child: const Text("Add"),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  );
                },
              );
            },
            icon: const Icon(Icons.add),
          ),
        ],
        leading: Builder(
          builder: (context) => IconButton(
            onPressed: () {
              Scaffold.of(context).openDrawer();
            },
            icon: const Icon(Icons.menu),
          ),
        ),
      ),
      drawer: Drawer(
        child: ListView(
          children: [
            ListTile(
              title: const Text("Graph"),
              onTap: () {
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(builder: (context) => const Graph()),
                );
              },
            ),
          ],
        ),
      ),
      body: SafeArea(
        child: Stack(
          children: [
            Align(
              alignment: Alignment.center,
              child: SizedBox(
                width: 300,
                height: 300,
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    SizedBox(
                      width: 300,
                      height: 300,
                      child: TweenAnimationBuilder(
                        tween: Tween(
                            begin: 1.0,
                            end: originalTime > 0
                                ? currentTime / originalTime
                                : 0.0),
                        duration: Duration(seconds: 1),
                        builder: (BuildContext context, double value,
                            Widget? child) {
                          return CircularProgressIndicator(
                            value: value,
                            strokeWidth: 10,
                          );
                        },
                      ),
                    ),
                    Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          decoration: BoxDecoration(
                            color: workTypeColor(tasks[currentTask]["type"]),
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Padding(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 10,
                              vertical: 2,
                            ),
                            child: Text(
                              workTypeTransform(tasks[currentTask]["type"]),
                            ),
                          ),
                        ),
                        Text(
                          timerFormatter(currentTime),
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 40,
                          ),
                        ),
                        Text(tasks[currentTask]["name"]),
                        IconButton(
                          onPressed: () {
                            if (isCounting) {
                              pause();
                            } else {
                              start();
                            }
                          },
                          icon: Icon(
                            isCounting ? Icons.pause : Icons.play_arrow,
                            size: 60,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
