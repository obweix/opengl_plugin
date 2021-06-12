import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:opengl_plg/opengl_plg.dart';

void main() {
  const MethodChannel channel = MethodChannel('opengl_plg');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await OpenglPlg.platformVersion, '42');
  });
}
