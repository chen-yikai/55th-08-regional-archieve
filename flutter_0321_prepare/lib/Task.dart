class TaskList {
  TaskList._internal();

  static final TaskList _instance = TaskList._internal();

  factory TaskList() => _instance;

  List<ListSchema> data = [];
}

class ListSchema {
  final String name;
  final int seconds;
  final String type;
  final int day;

  const ListSchema(this.name, this.seconds, this.type, this.day);
}

final List<String> days = [
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
  "Sunday"
];

final List<String> type = ["Work", "Rest"];