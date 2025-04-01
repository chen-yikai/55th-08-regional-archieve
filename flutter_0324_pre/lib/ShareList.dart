class ShareList {
  ShareList._internal();

  static final ShareList _instance = ShareList._internal();

  factory ShareList() => _instance;

  List<TodoList> data = [
    TodoList(83923, "hello", 10, 0, 1),
    TodoList(8323, "kitty", 10, 1, 1),
    TodoList(8393, "meow", 20, 2, 1),
    TodoList(3923, "good", 50, 3, 1),
    TodoList(323, "wow", 5, 4, 1),
    TodoList(32113, "wow", 5, 4, 1),
    TodoList(341923, "yay", 10, 5, 1),
  ];
}

class TodoList {
  final int id;
  final String name;
  final int time;
  final int type;
  final int day;

  const TodoList(this.id, this.name, this.time, this.day, this.type);
}

final typeOpt = ["Work", "Rest"];

final dayOpt = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
