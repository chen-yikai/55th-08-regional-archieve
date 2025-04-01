import 'package:flutter/material.dart';
import 'package:flutter_yang_toast/Share.dart';
import 'package:flutter_yang_toast/TodoList.dart';

void AllList(BuildContext context, Function callback) {
  var todoList = TaskList();
  showModalBottomSheet(
      context: context,
      builder: (context) {
        return StatefulBuilder(
          builder: (BuildContext context, setState) => Container(
            width: double.infinity,
            height: double.infinity,
            decoration: BoxDecoration(borderRadius: BorderRadius.circular(20)),
            child: Column(
              children: [
                Dragger(),
                SizedBox(height: 10),
                Text(
                  "All Todo",
                  style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
                ),
                Expanded(
                  child: ReorderableListView.builder(
                    itemCount: todoList.data.length,
                    itemBuilder: (context, index) {
                      final it = todoList.data[index];
                      return Dismissible(
                        key: ValueKey(it.id),
                        onDismissed: (direction) {
                          if (direction == DismissDirection.endToStart) {
                            setState(() {
                              TaskList().data.removeAt(index);
                            });
                            callback();
                          }
                        },
                        background: Container(
                          color: Colors.redAccent,
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.end,
                            children: [
                              Padding(
                                padding: const EdgeInsets.only(right: 15),
                                child: Icon(Icons.delete),
                              ),
                            ],
                          ),
                        ),
                        child: ListTile(
                          title: Text(it.name),
                          subtitle: Text(it.type),
                          trailing: Text(
                            timeFormatter(it.seconds),
                            style: TextStyle(fontSize: 20, color: Colors.grey),
                          ),
                        ),
                      );
                    },
                    onReorder: (int oldIndex, int newIndex) {
                      if (oldIndex < newIndex) {
                        newIndex -= 1;
                      }
                      final item = todoList.data.removeAt(oldIndex);
                      todoList.data.insert(newIndex, item);
                      todoList = TaskList();
                      callback();
                    },
                  ),
                ),
              ],
            ),
          ),
        );
      });
}
