import 'package:flutter/material.dart';

import 'home.dart';
import 'splash.dart';

void main() {
  runApp(MaterialApp(debugShowCheckedModeBanner: false, home: SplashScreen()));
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
  var splashScreen = Text("Splash Screen");
}

class _HomePageState extends State<HomePage>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Flutter TabBar Example'),
        bottom: TabBar(
          controller: _tabController,
          tabs: [
            Tab(icon: Icon(Icons.home), text: "Home"),
            Tab(icon: Icon(Icons.star), text: "Favorites"),
            Tab(icon: Icon(Icons.person), text: "Profile"),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          Center(
              child: Stack(children: [
            Container(
              constraints: BoxConstraints.expand(),
              child: Image.asset("assets/background.png"),
            ),
            Text('Home Screen')
          ])),
          Center(child: home()),
          Center(child: Text('Profile Screen')),
        ],
      ),
    );
  }
}
