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
  var data = [];

  @override
  void initState() {
    xmlParsing();
    super.initState();
  }

  Future<void> xmlParsing() async {
    final xmlString = await rootBundle.loadString('assets/data.xml');
    List<Map<String, String>> stations = parseStations(xmlString);

    for (int i = 0; i < 3 && i < stations.length; i++) {
      print('Station $i: ${stations[i]}');
    }
  }

  List<Map<String, String>> parseStations(String xml) {
    List<Map<String, String>> stations = [];

    String results = xml.split('<results>')[1].split('</results>')[0];

    List<String> stationBlocks = results
        .split('</station>')
        .where((s) => s.contains('<station>'))
        .toList();

    for (String block in stationBlocks) {
      Map<String, String> station = {};

      station['id'] = block.split('<id>')[1].split('</id>')[0].trim();
      station['station'] =
          block.split('<Station>')[1].split('</Station>')[0].trim();
      station['destination'] =
          block.split('<Destination>')[1].split('</Destination>')[0].trim();
      station['updateTime'] =
          block.split('<UpdateTime>')[1].split('</UpdateTime>')[0].trim();
      station['lineColor'] =
          block.split('<LineColor>')[1].split('</LineColor>')[0].trim();
      stations.add(station);
    }
    setState(() {
      data = stations;
    });
    return stations;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Column(
          children: [
            Expanded(
                child: ListView.builder(
                    itemCount: data.length,
                    itemBuilder: (context, index) {
                      final it = data[index];
                      return ListTile(
                        title: Text(it['station']),
                      );
                    }))
          ],
        ),
      ),
    );
  }
}
