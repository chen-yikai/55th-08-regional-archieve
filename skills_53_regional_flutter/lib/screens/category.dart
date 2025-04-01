import 'package:flutter/material.dart';
import 'package:skills_53_regional_flutter/components/color.dart';

class CategoryScreen extends StatefulWidget {
  final GlobalKey<ScaffoldState> scaffoldKey;
  const CategoryScreen({super.key, required this.scaffoldKey});

  @override
  State<CategoryScreen> createState() => _CategoryScreenState();
}

class _CategoryScreenState extends State<CategoryScreen> {
  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          color: blue,
          height: 150,
        )
      ],
    );
  }
}
