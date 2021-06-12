
import 'dart:async';

import 'package:flutter/services.dart';

class OpenglPlg {
  static const MethodChannel _channel =
  const MethodChannel('opengl_plg');

  int textureId = -1;

  Future<int> initialize(double width, double height) async {
    textureId = await _channel.invokeMethod('create', {
      'width': width,
      'height': height,
    });
    return textureId;
  }

  Future<Null> dispose() =>
      _channel.invokeMethod('dispose', {'textureId': textureId});

  bool get isInitialized => textureId != null;
}
