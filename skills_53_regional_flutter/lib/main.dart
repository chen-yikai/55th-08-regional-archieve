import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:skills_53_regional_flutter/components/drawer.dart';
import 'package:skills_53_regional_flutter/screens/about.dart';
import 'package:skills_53_regional_flutter/screens/category.dart';

import 'screens/home.dart';
import 'screens/news.dart';
import 'screens/splash.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  @override
  Widget build(BuildContext context) {
    final router = GoRouter(routes: [
      GoRoute(
          path: '/home',
          name: "home",
          builder: (context, state) => AppDrawer(
              scaffoldKey: _scaffoldKey,
              child: HomeScreen(scaffoldKey: _scaffoldKey))),
      GoRoute(
          path: '/',
          name: "splash",
          builder: (context, state) => SplashScreen()),
      GoRoute(
          path: '/news',
          name: "news",
          builder: (context, state) => AppDrawer(
              scaffoldKey: _scaffoldKey,
              child: NewsScreen(scaffoldKey: _scaffoldKey))),
      GoRoute(
          path: '/category',
          name: "category",
          builder: (context, state) => AppDrawer(
              scaffoldKey: _scaffoldKey,
              child: CategoryScreen(scaffoldKey: _scaffoldKey))),
      GoRoute(
          path: '/about',
          name: "about",
          builder: (context, state) => AppDrawer(
              scaffoldKey: _scaffoldKey,
              child: AboutScreen(scaffoldKey: _scaffoldKey)))
    ]);
    return MaterialApp.router(
      routerConfig: router,
      debugShowCheckedModeBanner: false,
    );
  }
}
