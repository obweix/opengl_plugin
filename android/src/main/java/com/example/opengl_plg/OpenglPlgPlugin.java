package com.example.opengl_plg;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import android.graphics.SurfaceTexture;
import android.util.LongSparseArray;
import java.util.Map;
import io.flutter.view.TextureRegistry;



//import android.annotation.TargetApi;
//import android.graphics.SurfaceTexture;
//import android.opengl.GLES20;
//import android.os.Build;
//import android.util.Log;
//
//import android.util.LongSparseArray;
//
//import androidx.annotation.NonNull;
//
//import android.opengl.GLUtils;
//
//import javax.microedition.khronos.egl.EGL10;
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.egl.EGLContext;
//import javax.microedition.khronos.egl.EGLDisplay;
//import javax.microedition.khronos.egl.EGLSurface;
//
//import java.util.Map;
//
//import io.flutter.embedding.engine.plugins.FlutterPlugin;
//import io.flutter.plugin.common.MethodCall;
//import io.flutter.plugin.common.MethodChannel;
//import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
//import io.flutter.plugin.common.MethodChannel.Result;
//import io.flutter.plugin.common.PluginRegistry.Registrar;
//
//
//import io.flutter.view.TextureRegistry;

/** OpenglPlgPlugin */
public class OpenglPlgPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  // my textures
  private  TextureRegistry textures ;
  private LongSparseArray<OpenGLRenderer> renders = new LongSparseArray<>();

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "opengl_plg");
    channel.setMethodCallHandler(this);

    // 参考flutter官方提供的video_playe插件源码
    this.textures = flutterPluginBinding.getTextureRegistry();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

    Map<String, Number> arguments = (Map<String, Number>) call.arguments;
    if (call.method.equals("create")) {
      TextureRegistry.SurfaceTextureEntry entry = textures.createSurfaceTexture();
      SurfaceTexture surfaceTexture = entry.surfaceTexture();

      int width = arguments.get("width").intValue();
      int height = arguments.get("height").intValue();
      surfaceTexture.setDefaultBufferSize(width, height);

      SampleRenderWorker worker = new SampleRenderWorker();
      OpenGLRenderer render = new OpenGLRenderer(surfaceTexture, worker);
      renders.put(entry.id(), render);

      result.success(entry.id());
    } else if (call.method.equals("dispose")) {
      long textureId = arguments.get("textureId").longValue();
      OpenGLRenderer render = renders.get(textureId);
      render.onDispose();

      renders.delete(textureId);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
