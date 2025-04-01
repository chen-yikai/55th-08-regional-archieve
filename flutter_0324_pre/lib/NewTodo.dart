import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_0324_pre/ShareList.dart';

void NewTodo(BuildContext context, Function callback) {
  final name = TextEditingController(),
      mm = TextEditingController(),
      ss = TextEditingController();
  int typeSelect = 0;
  int daySelect = 0;
  showModalBottomSheet(
    context: context,
    builder: (BuildContext context) {
      return StatefulBuilder(
        builder: (context, setState) => Container(
          margin: EdgeInsets.all(20),
          width: double.infinity,
          child: Column(
            children: [
              TextField(
                controller: name,
                onChanged: (f) {
                  setState(() {});
                },
                decoration: InputDecoration(
                    border: OutlineInputBorder(), hintText: "Name"),
              ),
              SizedBox(height: 20),
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: mm,
                      decoration: InputDecoration(
                          border: OutlineInputBorder(), hintText: "Minutes"),
                    ),
                  ),
                  SizedBox(width: 20),
                  Expanded(
                    child: TextField(
                      controller: ss,
                      decoration: InputDecoration(
                          border: OutlineInputBorder(), hintText: "Seconds"),
                    ),
                  )
                ],
              ),
              SizedBox(height: 20),
              Row(
                children: [
                  Expanded(
                    child: DropdownMenu(
                      dropdownMenuEntries: typeOpt
                          .map((item) => DropdownMenuEntry(
                              value: typeOpt.indexOf(item), label: item))
                          .toList(),
                      onSelected: (i) {
                        typeSelect = i ?? 0;
                      },
                    ),
                  ),
                  Expanded(
                    child: DropdownMenu(
                      dropdownMenuEntries: dayOpt
                          .map((item) => DropdownMenuEntry(
                              value: typeOpt.indexOf(item), label: item))
                          .toList(),
                      onSelected: (i) {
                        daySelect = i ?? 0;
                      },
                    ),
                  ),
                ],
              ),
              ElevatedButton(
                  onPressed: () {
                    setState(() {
                      ShareList().data.add(TodoList(
                          Random().nextInt(1000),
                          name.text,
                          (int.tryParse(mm.text) ?? 0 * 60) +
                              (int.tryParse(ss.text) ?? 0),
                          typeSelect,
                          daySelect));
                    });
                    callback();
                    Navigator.pop(context);
                  },
                  child: Text("Add"))
            ],
          ),
        ),
      );
    },
  );
}
