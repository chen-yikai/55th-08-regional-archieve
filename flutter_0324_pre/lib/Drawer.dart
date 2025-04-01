import 'package:flutter/material.dart';
import 'package:flutter_0324_pre/Graph.dart';

Drawer RootDrawer(BuildContext context) {
  return Drawer(
    child: ListView(
      children: [
        ListTile(
          title: Text("Graph"),
          onTap: () {
            Navigator.push(context,
                MaterialPageRoute(builder: (context) => GraphScreen()));
          },
        )
      ],
    ),
  );
}
