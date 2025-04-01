import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';

void main() {
  runApp(const App());
}

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  var data = [];
  final searchController = TextEditingController();

  @override
  void initState() {
    fetcher("");
    super.initState();
  }

  Future<void> fetcher(String search) async {
    String url = "https://skills-music-api-v2.eliaschen.dev/sounds";
    HttpClient client = HttpClient();

    try {
      final request = await client.getUrl(Uri.parse(url));
      request.headers.add('search', search);
      final response = await request.close();
      if (response.statusCode == 200) {
        final string = await response.transform(utf8.decoder).join();
        data = jsonDecode(string);
      }
      setState(() {});
    } catch (e) {}
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: SafeArea(
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.all(15),
                child: SearchBar(
                  controller: searchController,
                  onChanged: (value) {
                    fetcher(value);
                  },
                ),
              ),
              Expanded(
                child: ListView.builder(
                  itemCount: data.length,
                  itemBuilder: (context, index) {
                    var tags = data[index]['tags'];
                    return Column(
                      children: [
                        ListTile(
                          title: Text(data[index]['name']),
                          subtitle: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(data[index]['description']),
                              SingleChildScrollView(
                                scrollDirection: Axis.horizontal,
                                child: Row(
                                  children: tags
                                      .map<Widget>((item) => Padding(
                                            padding: const EdgeInsets.only(
                                                right: 5, top: 10),
                                            child: Container(
                                              decoration: BoxDecoration(
                                                  color: Color(0xffFFA0EC),
                                                  borderRadius:
                                                      BorderRadius.circular(
                                                          10)),
                                              child: Padding(
                                                padding:
                                                    const EdgeInsets.symmetric(
                                                        horizontal: 10,
                                                        vertical: 4),
                                                child: Text(item),
                                              ),
                                            ),
                                          ))
                                      .toList(),
                                ),
                              ),
                            ],
                          ),
                        ),
                        Divider()
                      ],
                    );
                  },
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
