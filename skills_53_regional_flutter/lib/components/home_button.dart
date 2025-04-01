import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import 'color.dart';

class HomeButton extends StatelessWidget {
  final String text;
  final String icon;
  final String target;

  const HomeButton(
      {super.key,
      required this.text,
      required this.icon,
      required this.target});

  @override
  Widget build(BuildContext context) {
    return Container(
        decoration: BoxDecoration(
          boxShadow: const [
            BoxShadow(
              color: Colors.black26,
              offset: Offset(0, 4),
              blurRadius: 4.0,
            ),
          ],
          borderRadius: BorderRadius.circular(20),
        ),
        margin: const EdgeInsets.only(bottom: 30),
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),
          ),
          onPressed: () {
            context.goNamed(target);
          },
          child: Stack(
            children: [
              Align(
                alignment: Alignment.centerLeft,
                child: Image.asset("assets/icons/$icon", height: 25),
              ),
              Center(
                child: Text(
                  text,
                  style: const TextStyle(
                      fontSize: 20, color: blue, fontWeight: FontWeight.bold),
                ),
              ),
            ],
          ),
        ));
  }
}
