import 'package:flutter/material.dart';

import '../components/home_button.dart';

class HomeScreen extends StatefulWidget {
  final GlobalKey<ScaffoldState> scaffoldKey;

  const HomeScreen({super.key, required this.scaffoldKey});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  Widget build(BuildContext context) {
    return Stack(children: [
      Expanded(child: Image.asset('assets/bg.png')),
      SafeArea(
          child: Padding(
              padding: const EdgeInsets.all(10),
              child: IconButton(
                  onPressed: () {},
                  icon: Image.asset('assets/icons/menu.png', height: 40)))),
      Padding(
        padding: const EdgeInsets.all(60),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Image.asset('assets/home_logo.png', height: 100),
            const SizedBox(height: 100),
            const Column(children: [
              HomeButton(text: '最新消息', icon: 'home.png', target: 'news'),
              HomeButton(
                text: '關於競賽',
                icon: 'people.png',
                target: "about",
              ),
              HomeButton(
                text: '職類介紹',
                icon: 'cup.png',
                target: "category",
              ),
            ]),
          ],
        ),
      ),
    ]);
  }
}
