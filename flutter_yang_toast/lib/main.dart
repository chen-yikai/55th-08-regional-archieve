import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_yang_toast/AllList.dart';
import 'package:flutter_yang_toast/HomeTopBar.dart';
import 'package:flutter_yang_toast/Share.dart';
import 'package:flutter_yang_toast/TodoList.dart';

void main() {
  runApp(const MaterialApp(
    debugShowCheckedModeBanner: false,
    home: Home(),
  ));
}

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  late Timer timer;
  int count = 20;
  int ogCount = 20;
  bool isCounting = false;
  final double sheetHeight = 0.3;
  var todoList = TaskList();
  int currentTodo = 0;
  late ListSchema currentTodoList;
  var allDone = false;

  @override
  void initState() {
    super.initState();
    initTodo(0);
  }

  void initTodo(index) {
    setState(() {
      if (todoList.data.isNotEmpty) {
        currentTodoList = todoList.data[index];
        count = currentTodoList.seconds;
        ogCount = currentTodoList.seconds;
      } else {
        count = 0;
        ogCount = 0;
      }
    });
  }

  void disposeTodo() {
    setState(() {
      count = 0;
      ogCount = 0;
    });
  }

  void start() {
    if (!isCounting) {
      setState(() {
        isCounting = true;
      });
      timer = Timer.periodic(Duration(seconds: 1), (timerCurrent) {
        setState(() {
          if (count > 0) {
            count--;
          } else if (currentTodo != todoList.data.length - 1) {
            currentTodo++;
            initTodo(currentTodo);
          } else if (currentTodo == todoList.data.length - 1) {
            allDone = true;
          } else {
            isCounting = false;
            timerCurrent.cancel();
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: HomeAppBar(context, () {
        setState(() {});
      }),
      bottomSheet: DraggableScrollableSheet(
          expand: false,
          minChildSize: sheetHeight,
          maxChildSize: sheetHeight,
          initialChildSize: sheetHeight,
          builder: (context, controller) {
            return SingleChildScrollView(
              controller: controller,
              child: Container(
                width: double.infinity,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Column(
                  children: [
                    Dragger(),
                    SizedBox(
                      height:
                          MediaQuery.of(context).size.height * sheetHeight - 40,
                      child: ListView.builder(
                          itemCount: todoList.data.length,
                          itemBuilder: (context, index) {
                            final it = todoList.data[index];
                            return index < 3
                                ? ListTile(
                                    title: Text(it.name),
                                    subtitle: Text(it.type),
                                    trailing: Text(
                                      timeFormatter(it.seconds),
                                      style: TextStyle(
                                          fontSize: 20, color: Colors.grey),
                                    ),
                                  )
                                : SizedBox();
                          }),
                    )
                  ],
                ),
              ),
            );
          }),
      body: GestureDetector(
        onVerticalDragUpdate: (detail) {
          if (detail.delta.dy < -5) {
            AllList(context, () {
              setState(() {
                currentTodo = 0;
                if (isCounting) {
                  timer.cancel();
                  isCounting = false;
                }
                allDone = false;
                initTodo(0);
              });
            });
          }
        },
        child: Padding(
          padding: const EdgeInsets.only(top: 200),
          child: Column(
            children: [
              Center(
                child: SizedBox(
                  width: 300,
                  height: 300,
                  child: Stack(
                    children: [
                      SizedBox(
                        width: 300,
                        height: 300,
                        child: TweenAnimationBuilder(
                          tween: Tween(
                              begin: 1.0,
                              end: count > 0 ? count / ogCount : 1.0),
                          duration: const Duration(seconds: 1),
                          builder: (context, value, widget) {
                            return CircularProgressIndicator(
                              value: value,
                              strokeWidth: 15,
                            );
                          },
                        ),
                      ),
                      Center(
                        child: todoList.data.isNotEmpty
                            ? Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  if (!allDone)
                                    Container(
                                        decoration: BoxDecoration(
                                            borderRadius:
                                                BorderRadius.circular(20),
                                            color:
                                                currentTodoList.type == "Rest"
                                                    ? Color(0xffFF6F83)
                                                    : Color(0xffC89BFF)),
                                        child: Padding(
                                          padding: const EdgeInsets.symmetric(
                                              horizontal: 10, vertical: 4),
                                          child: Text(currentTodoList.type),
                                        )),
                                  Text(
                                    timeFormatter(count),
                                    style: TextStyle(
                                        fontSize: 50,
                                        fontWeight: FontWeight.bold),
                                  ),
                                  if (!allDone) Text(currentTodoList.name),
                                  IconButton(
                                      onPressed: () {
                                        if (isCounting) {
                                          pause();
                                        } else {
                                          start();
                                        }
                                      },
                                      icon: isCounting
                                          ? Icon(
                                              Icons.pause,
                                              size: 70,
                                            )
                                          : Icon(
                                              Icons.play_arrow,
                                              size: 70,
                                            ))
                                ],
                              )
                            : Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Text(
                                    "No Data",
                                    style: TextStyle(
                                        fontSize: 40,
                                        fontWeight: FontWeight.bold),
                                  )
                                ],
                              ),
                      )
                    ],
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
