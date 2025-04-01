import 'package:csv/csv.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:gap/gap.dart';
// import 'package:google_fonts/google_fonts.dart';
import 'package:skills_53_regional_flutter/components/color.dart';

class NewsScreen extends StatefulWidget {
  final GlobalKey<ScaffoldState> scaffoldKey;

  const NewsScreen({super.key, required this.scaffoldKey});

  @override
  State<NewsScreen> createState() => _NewsScreenState();
}

class _NewsScreenState extends State<NewsScreen> {
  List<List<dynamic>> data = [];

  @override
  void initState() {
    super.initState();
    loadcsv();
  }

  Future<void> loadcsv() async {
    final csvData = await rootBundle.loadString('assets/data/news.csv');
    List<List<dynamic>> listData = const CsvToListConverter().convert(csvData);

    listData.sort((a, b) {
      DateTime dateA = DateTime.parse(a[1]);
      DateTime dateB = DateTime.parse(b[1]);
      return dateB.compareTo(dateA);
    });

    setState(() {
      data = listData;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Expanded(child: Image.asset("assets/bg.png")),
          Container(color: blue, height: 150),
          SafeArea(
              child: Padding(
                  padding: const EdgeInsets.only(left: 20, top: 20, right: 20),
                  child: Stack(
                    children: [
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
                                text: "最新",
                                style: topBarTextStyle.copyWith(
                                    fontWeight: FontWeight.normal)),
                            TextSpan(
                                text: "消息",
                                style: topBarTextStyle.copyWith(
                                    fontWeight: FontWeight.bold))
                          ]))),
                      Container(
                          padding: const EdgeInsets.only(top: 70),
                          child: Center(
                            child: data.isEmpty
                                ? const CircularProgressIndicator()
                                : ListView.builder(
                                    itemCount: data.length,
                                    itemBuilder: (context, index) {
                                      return Container(
                                          decoration: BoxDecoration(
                                            boxShadow: const [
                                              BoxShadow(
                                                color: Colors.black26,
                                                offset: Offset(0, 4),
                                                blurRadius: 4.0,
                                              ),
                                            ],
                                            borderRadius:
                                                BorderRadius.circular(20),
                                          ),
                                          margin:
                                              const EdgeInsets.only(bottom: 20),
                                          child: ElevatedButton(
                                              onPressed: () => showDialog(
                                                    useSafeArea: false,
                                                    context: context,
                                                    builder: (context) {
                                                      return Dialog.fullscreen(
                                                          child: Stack(
                                                              children: [
                                                            Expanded(
                                                                child: Image.asset(
                                                                    "assets/bg.png")),
                                                            SafeArea(
                                                              child: Container(
                                                                  margin:
                                                                      const EdgeInsets
                                                                          .only(
                                                                          left:
                                                                              10),
                                                                  child:
                                                                      IconButton(
                                                                          onPressed:
                                                                              () {
                                                                            Navigator.pop(context);
                                                                          },
                                                                          icon: Image.asset(
                                                                              "assets/icons/blue_arrow_left.png",
                                                                              height: 15))),
                                                            ),
                                                            SafeArea(
                                                                child: Padding(
                                                                    padding: const EdgeInsets
                                                                        .only(
                                                                        top: 50,
                                                                        bottom:
                                                                            20,
                                                                        right:
                                                                            20,
                                                                        left:
                                                                            20),
                                                                    child:
                                                                        SingleChildScrollView(
                                                                            child: Column(
                                                                                mainAxisAlignment: MainAxisAlignment.start,
                                                                                crossAxisAlignment: CrossAxisAlignment.start,
                                                                                children: [
                                                                          Text(
                                                                            data[index][0].toString(),
                                                                            style: const TextStyle(
                                                                                color: blue,
                                                                                fontSize: 20,
                                                                                fontWeight: FontWeight.bold),
                                                                          ),
                                                                          Text(
                                                                            data[index][1].toString(),
                                                                            style:
                                                                                const TextStyle(
                                                                              fontSize: 10,
                                                                              fontFamily: "monospace",
                                                                              color: Colors.black45,
                                                                              fontWeight: FontWeight.bold,
                                                                            ),
                                                                          ),
                                                                          const Gap(
                                                                              20),
                                                                          Text(data[index][2]
                                                                              .toString())
                                                                        ]))))
                                                          ]));
                                                    },
                                                  ),
                                              style: ElevatedButton.styleFrom(
                                                padding:
                                                    const EdgeInsets.symmetric(
                                                        horizontal: 15,
                                                        vertical: 10),
                                                backgroundColor: Colors.white,
                                                shape: RoundedRectangleBorder(
                                                  borderRadius:
                                                      BorderRadius.circular(10),
                                                ),
                                              ),
                                              child: Align(
                                                  alignment:
                                                      Alignment.centerLeft,
                                                  child: Column(
                                                      mainAxisAlignment:
                                                          MainAxisAlignment
                                                              .start,
                                                      crossAxisAlignment:
                                                          CrossAxisAlignment
                                                              .start,
                                                      children: [
                                                        Text(
                                                          data[index][1]
                                                              .toString(),
                                                          style: const TextStyle(
                                                              fontSize: 13,
                                                              fontFamily:
                                                                  "monospace",
                                                              color: gray,
                                                              fontWeight:
                                                                  FontWeight
                                                                      .bold),
                                                        ),
                                                        const Gap(5),
                                                        Text(
                                                            data[index][0]
                                                                .toString(),
                                                            style: const TextStyle(
                                                                fontSize: 15,
                                                                fontWeight:
                                                                    FontWeight
                                                                        .bold,
                                                                color: blue)),
                                                      ]))));
                                    }),
                          ))
                    ],
                  )))
        ],
      ),
    );
  }

  final topBarTextStyle =
      const TextStyle(fontSize: 25, letterSpacing: 5, color: Colors.white);
}
