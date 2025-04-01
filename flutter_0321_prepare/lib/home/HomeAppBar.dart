import 'package:flutter/material.dart';
import 'package:flutter_0321_prepare/Task.dart';

PreferredSizeWidget HomeAppBar(BuildContext context) {
  final mm = TextEditingController(),
      ss = TextEditingController(),
      name = TextEditingController();
  String? typeValue = type[0];
  String? daysValue = days[0];
  TaskList taskList = TaskList();

  return AppBar(
    title: Text("Timer"),
    actions: [
      IconButton(
          onPressed: () {
            showModalBottomSheet(
                context: context,
                builder: (BuildContext context) {
                  return StatefulBuilder(
                    builder: (context, setState) => Container(
                      decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(20),
                          color: Colors.white),
                      width: double.infinity,
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 20),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Column(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(10),
                                  child: Container(
                                    width: 100,
                                    height: 5,
                                    decoration: BoxDecoration(
                                        color: Colors.grey,
                                        borderRadius:
                                            BorderRadius.circular(10)),
                                  ),
                                ),
                                SizedBox(height: 20),
                                Text(
                                  "Add Todo",
                                  style: TextStyle(
                                      fontSize: 30,
                                      fontWeight: FontWeight.bold),
                                ),
                                SizedBox(height: 20),
                                CustomInput(name, "Name"),
                                SizedBox(height: 20),
                                Row(
                                  children: [
                                    Expanded(child: CustomInput(mm, "Minutes")),
                                    SizedBox(width: 20),
                                    Expanded(child: CustomInput(ss, "Seconds")),
                                  ],
                                ),
                                SizedBox(
                                  height: 20,
                                ),
                                Row(
                                  children: [
                                    DropdownButton(
                                        value: typeValue,
                                        items: type
                                            .map((item) => DropdownMenuItem(
                                                value: item, child: Text(item)))
                                            .toList(),
                                        onChanged: (value) {
                                          setState(() {
                                            typeValue = value;
                                          });
                                        }),
                                    SizedBox(
                                      width: 20,
                                    ),
                                    DropdownMenu(
                                      label: Text("Days"),
                                      dropdownMenuEntries: days
                                          .map((item) => DropdownMenuEntry(
                                                label: item,
                                                value: item,
                                              ))
                                          .toList(),
                                      onSelected: (value) {
                                        setState(() {
                                          daysValue = value;
                                        });
                                      },
                                    )
                                  ],
                                )
                              ],
                            ),
                            Row(
                              children: [
                                Expanded(
                                  child: ElevatedButton(
                                      onPressed: () {
                                        taskList.data.add(
                                          ListSchema(
                                              name.text,
                                              ((int.tryParse(mm.text) ?? 0) *
                                                      60 +
                                                  (int.tryParse(ss.text) ?? 0)),
                                              typeValue!,
                                              days.indexOf(daysValue!)),
                                        );
                                        Navigator.pop(context);
                                      },
                                      child: Text("Add")),
                                ),
                              ],
                            )
                          ],
                        ),
                      ),
                    ),
                  );
                });
          },
          icon: Icon(Icons.add))
    ],
  );
}

TextField CustomInput(TextEditingController controller, String text) {
  return TextField(
    controller: controller,
    decoration:
        InputDecoration(border: const OutlineInputBorder(), hintText: text),
  );
}
