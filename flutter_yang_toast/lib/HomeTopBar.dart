import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_yang_toast/Share.dart';
import 'package:flutter_yang_toast/TodoList.dart';

PreferredSizeWidget HomeAppBar(BuildContext context, Function callback) {
  final name = TextEditingController(),
      mm = TextEditingController(),
      ss = TextEditingController();
  var type;

  final todoList = TaskList();

  return AppBar(
    title: Text("Eugene Go"),
    actions: [
      IconButton(
          onPressed: () {
            showModalBottomSheet(
                context: context,
                builder: (context) {
                  return Container(
                    width: double.infinity,
                    height: 400,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(20),
                        color: Colors.white),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      child: Column(
                        children: [
                          Dragger(),
                          SizedBox(height: 10),
                          Text("Add Todo",
                              style: TextStyle(
                                  fontSize: 30, fontWeight: FontWeight.bold)),
                          SizedBox(height: 20),
                          CustomInput(name, "Name"),
                          SizedBox(height: 20),
                          Row(
                            children: [
                              Expanded(child: CustomInput(mm, "Minutes")),
                              SizedBox(width: 20),
                              Expanded(child: CustomInput(ss, "Seconds"))
                            ],
                          ),
                          SizedBox(height: 20),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              DropdownMenu(
                                  onSelected: (item) {
                                    type = item;
                                  },
                                  dropdownMenuEntries: typeValue
                                      .map((item) => DropdownMenuEntry(
                                          value: item, label: item))
                                      .toList()),
                            ],
                          ),
                          SizedBox(height: 20),
                          Row(
                            children: [
                              Expanded(
                                  child: ElevatedButton(
                                      onPressed: () {
                                        final int seconds =
                                            int.tryParse(ss.text) ??
                                                0 +
                                                    (int.tryParse(mm.text) ??
                                                            0) *
                                                        60;

                                        todoList.data.add(ListSchema(
                                            Random().nextInt(100),
                                            name.text,
                                            seconds,
                                            type));
                                        callback();
                                        Navigator.pop(context);
                                      },
                                      child: Text("Add"))),
                            ],
                          )
                        ],
                      ),
                    ),
                  );
                });
          },
          icon: Icon(Icons.add))
    ],
  );
}

Widget CustomInput(TextEditingController controller, String hint) {
  return TextField(
      controller: controller,
      decoration:
          InputDecoration(border: OutlineInputBorder(), hintText: hint));
}
