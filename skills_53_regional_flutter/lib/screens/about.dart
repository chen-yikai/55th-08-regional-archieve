import 'dart:convert';
import 'package:csv/csv.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:skills_53_regional_flutter/components/color.dart';

class AboutScreen extends StatefulWidget {
  final GlobalKey<ScaffoldState> scaffoldKey;
  const AboutScreen({super.key, required this.scaffoldKey});
  @override
  State<AboutScreen> createState() => _AboutScreenState();
}

class _AboutScreenState extends State<AboutScreen>
    with SingleTickerProviderStateMixin {
  List<List<dynamic>> listData = [];
  List<List<String>> imageData = [];

  Future<void> fileGetter(index) async {
    final String manifestContent =
        await rootBundle.loadString('AssetManifest.json');
    final Map<String, dynamic> manifestMap = json.decode(manifestContent);
    String directoryPath = 'assets/data/about/$index';
    List<String> tempData = [];
    manifestMap.forEach((key, item) {
      if (key.startsWith(directoryPath)) {
        tempData.add(key);
      }
    });
    imageData.add(tempData);
  }

  Future<void> loadcsv() async {
    final csvData = await rootBundle.loadString('assets/data/about/about.csv');
    listData = const CsvToListConverter().convert(csvData);
    setState(() {});
  }

  @override
  void initState() {
    super.initState();
    List.generate(5, (index) {
      fileGetter(index + 1);
    });
    loadcsv();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            flexibleSpace: Container(
          height: 200,
          color: blue,
          child: Stack(
            children: [
              Padding(
                  padding: const EdgeInsets.only(left: 10, right: 10, top: 50),
                  child: Column(children: [
                    Stack(children: [
                      Align(
                        alignment: Alignment.topLeft,
                        child: IconButton(
                            onPressed: () {
                              widget.scaffoldKey.currentState?.openDrawer();
                            },
                            icon: Image.asset(
                                "assets/icons/white_arrow_left.png",
                                height: 15)),
                      ),
                      Align(
                          alignment: Alignment.topCenter,
                          child: Text.rich(TextSpan(children: [
                            TextSpan(
                                text: "關於",
                                style: topBarTextStyle.copyWith(
                                    fontWeight: FontWeight.normal)),
                            TextSpan(
                                text: "競賽",
                                style: topBarTextStyle.copyWith(
                                    fontWeight: FontWeight.bold))
                          ]))),
                    ]),
                  ])),
            ],
          ),
        )),
        body: DefaultTabController(
            length: listData.length,
            child: Stack(children: [
              Column(
                children: [
                  Material(
                      color: blue,
                      child: TabBar(
                        labelPadding: const EdgeInsets.only(left: 10),
                        labelStyle: const TextStyle(
                            fontSize: 15, fontWeight: FontWeight.normal),
                        labelColor: Colors.white,
                        unselectedLabelColor: Colors.grey,
                        padding: const EdgeInsets.only(bottom: 10),
                        tabAlignment: TabAlignment.start,
                        isScrollable: true,
                        tabs: listData.map((entry) {
                          return Padding(
                              padding:
                                  const EdgeInsets.symmetric(horizontal: 10),
                              child: Tab(text: entry[0].toString()));
                        }).toList(),
                        indicator: const UnderlineTabIndicator(
                            borderRadius: BorderRadius.zero),
                      )),
                  Expanded(
                    child: Container(
                        decoration: const BoxDecoration(
                            image: DecorationImage(
                                image: AssetImage('assets/bg.png'),
                                fit: BoxFit.fill)),
                        child: TabBarView(
                          children: listData.asMap().entries.map((entry) {
                            final index = entry.key;
                            final item = entry.value;
                            return SingleChildScrollView(
                                child: SafeArea(
                                    child: Padding(
                                        padding: const EdgeInsets.symmetric(
                                            horizontal: 20, vertical: 15),
                                        child: Column(
                                          children: [
                                            Padding(
                                                padding: const EdgeInsets.only(
                                                    bottom: 20),
                                                child: SingleChildScrollView(
                                                    scrollDirection:
                                                        Axis.horizontal,
                                                    child: Row(
                                                      children: imageData[index]
                                                          .map((image) {
                                                        return Container(
                                                            padding:
                                                                const EdgeInsets
                                                                    .only(
                                                                    right: 10),
                                                            child: Image.asset(
                                                                image,
                                                                height: 200));
                                                      }).toList(),
                                                    ))),
                                            Text(item[1].toString(),
                                                style: const TextStyle(
                                                    color: gray))
                                          ],
                                        ))));
                          }).toList(),
                        )),
                  )
                ],
              )
            ])));
  }

  final topBarTextStyle =
      const TextStyle(fontSize: 25, letterSpacing: 5, color: Colors.white);
}
