import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import 'list_button.dart';

class AppDrawer extends StatefulWidget {
  final Widget child;
  final GlobalKey<ScaffoldState> scaffoldKey;

  const AppDrawer({super.key, required this.child, required this.scaffoldKey});

  @override
  State<AppDrawer> createState() => _AppDrawerState();
}

class _AppDrawerState extends State<AppDrawer> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: widget.scaffoldKey,
      drawer: Drawer(
        child: ListView(
          children: [
            Center(
                child: GestureDetector(
                    onTap: () async {
                      context.go("/home");
                      Future.delayed(const Duration(milliseconds: 500), () {
                        widget.scaffoldKey.currentState?.closeDrawer();
                      });
                    },
                    child: Container(
                      margin: const EdgeInsets.only(top: 50),
                      child: Image.asset('assets/nav_logo.png', height: 60),
                    ))),
            const SizedBox(height: 50),
            ListButton(
              title: "最新消息",
              icon: "home",
              current: false,
              target: 'news',
              scaffoldKey: widget.scaffoldKey,
            ),
            ListButton(
              title: "關於競賽",
              icon: "people",
              current: false,
              target: 'about',
              scaffoldKey: widget.scaffoldKey,
            ),
            ListButton(
                title: "職類介紹",
                icon: "cup",
                current: false,
                target: 'category',
                scaffoldKey: widget.scaffoldKey),
          ],
        ),
      ),
      body: widget.child,
    );
  }
}
