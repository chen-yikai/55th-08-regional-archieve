import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<Map<String, dynamic>> data = [];
  var searchController = TextEditingController();

  @override
  void initState() {
    parseXml();
    super.initState();
  }

  Future<void> parseXml() async {
    try {
      final xmlString = await rootBundle.loadString('assets/data.xml');
      final resultsInner =
          xmlString.split("<results>")[1].split("</results>")[0];
      final stationForeach =
          resultsInner.replaceAll("<station>", "").split("</station>").toList();

      for (var a in stationForeach) {
        if (a.trim().isEmpty) continue;

        String station = "Unknown";
        String destination = "Unknown";
        String updatetime = "Unknown";
        String linecolor = "Unknown";

        if (a.contains("<Station>") && a.contains("</Station>")) {
          station = a.split("<Station>")[1].split("</Station>")[0];
        }
        if (a.contains("<Destination>") && a.contains("</Destination>")) {
          destination = a.split("<Destination>")[1].split("</Destination>")[0];
        }
        if (a.contains("<UpdateTime>") && a.contains("</UpdateTime>")) {
          updatetime = a.split("<UpdateTime>")[1].split("</UpdateTime>")[0];
        }
        if (a.contains("<LineColor>") && a.contains("</LineColor>")) {
          linecolor = a.split("<LineColor>")[1].split("</LineColor>")[0];
        }

        data.add({
          "id": data.length,
          "station": station,
          "destination": destination,
          "updatetime": updatetime,
          "linecolor": linecolor,
        });
      }
      setState(() {});
    } catch (e) {
      print("error");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text("Metro"),
        ),
        body: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: SearchBar(
                controller: searchController,
                trailing: [
                  PopupMenuButton(itemBuilder: (context) {
                    final list = data.map((e) {
                      return PopupMenuItem(child: Text("${e['destination']}"));
                    });
                    return list.toList();
                  })
                ],
                onChanged: (f) {
                  setState(() {});
                },
              ),
            ),
            Expanded(
                child: ListView.builder(
                    itemCount: data.length,
                    itemBuilder: (context, index) {
                      final it = data[index];
                      final date = DateTime.parse(it['updatetime']);
                      return it['destination']
                              .toString()
                              .contains(searchController.text)
                          ? Dismissible(
                              key: ValueKey(it['id']),
                              background: Container(
                                color: Colors.redAccent,
                              ),
                              child: ListTile(
                                onTap: () {
                                  showDialog(
                                      context: context,
                                      builder: (context) {
                                        return Dialog(
                                            child: Padding(
                                                padding: EdgeInsets.all(20),
                                                child: Text(
                                                    "${it['station']} -> ${it['destination']}\n${it['linecolor'].toString().padLeft(2, "0")}\n${date.year}/${date.month.toString().padLeft(2, "0")}/${date.day} ${date.hour.toString().padLeft(2, "0")}:${date.minute.toString().padLeft(2, "0")}:${date.second.toString().padLeft(2, "0")}")));
                                      });
                                },
                                title: Text(
                                    "${it['station']} -> ${it['destination']}"),
                              ),
                            )
                          : SizedBox();
                    }))
          ],
        ),
      ),
    );
  }
}
