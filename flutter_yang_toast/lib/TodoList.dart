class TaskList {
  TaskList._internal();

  static final TaskList _instance = TaskList._internal();

  factory TaskList() => _instance;

  List<ListSchema> data = [
    ListSchema(33, "Feed my cat", 30, "Rest"),
    ListSchema(3, "Coding", 20, "Work"),
    ListSchema(7, "Cooking", 10, "Rest"),
    ListSchema(1, "Doing Homework", 30, "Work"),
    ListSchema(2, "Get", 5, "Work"),
    ListSchema(5, "Cooding", 10, "Work"),
    ListSchema(8, "Eating", 8, "Rest"),
  ];
}

class ListSchema {
  final int id;
  final String name;
  final int seconds;
  final String type;

  const ListSchema(this.id, this.name, this.seconds, this.type);
}

final typeValue = ["Work", "Rest"];
