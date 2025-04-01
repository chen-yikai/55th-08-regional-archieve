import 'dart:convert';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var data = [];
  var error = false;
  var theme = ThemeMode.system;

  @override
  void initState() {
    super.initState();
    fetch();
  }

  Future<void> fetch() async {
    setState(() {
      data = [];
      error = false;
    });
    final client = HttpClient();
    final url = Uri.parse("https://skills-flutter-test-api.eliaschen.dev/");
    try {
      final request = await client.getUrl(url);
      final response = await request.close();
      if (response.statusCode == 200) {
        final jsonString = await response.transform(utf8.decoder).join();
        final jsonData = jsonDecode(jsonString);
        setState(() {
          data = jsonData[0]['schedule'];
        });
      }
    } catch (e) {
      setState(() {
        error = true;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      themeMode: theme, // Automatically follows system theme
      theme: ThemeData(
        brightness: Brightness.light,
      ),
      darkTheme: ThemeData(
        brightness: Brightness.dark,
      ),
      home: Scaffold(
        appBar: AppBar(
          title: Text("API Request"),
          actions: [
            PopupMenuButton(
                itemBuilder: (context) => [
                      PopupMenuItem(
                        child: Text("System"),
                        onTap: () => setState(() {
                          theme = ThemeMode.system;
                        }),
                      ),
                      PopupMenuItem(
                        child: Text("Light"),
                        onTap: () => setState(() {
                          theme = ThemeMode.light;
                        }),
                      ),
                      PopupMenuItem(child: Text("Dark")),
                    ])
          ],
        ),
        body: Column(
          children: [
            Text("Data"),
            Expanded(
              child: ListView.builder(
                  itemCount: data.length,
                  itemBuilder: (context, index) {
                    return ListTile(title: Text("data"));
                  }),
            )
          ],
        ),
      ),
    );
  }
}
