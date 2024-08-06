/* I declare that this code is my own work, outside of the work implemented from the lab classes, mainly ch8 and scene graph work in ch7 */
/* Author Harry Akrill hakrill1@sheffield.ac.uk */
import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class SecurityPost {
  
  private ModelMultipleLights sphere;
  private Camera camera;
  private Light[] lights;
  private Texture t0, t1;
  private float size = 16f;

  // Scene graph variable declarations
  private SGNode securityRoot,securityPost;
  private TransformNode securityMoveTranslate,securityPostTransform,securityTopTranslate,securityTopTransform,rotateTop;

  private float xPosition = 0;
  private float rotateTopAngleStart = 50, rotateTopAngle = rotateTopAngleStart;

  public SecurityPost(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1) {
    camera = c;
    lights = l;
    this.t0 = t0;
    this.t1 = t1;
    securityPost = makeSecurityPost(gl);
    startTime = getSeconds();
  }

  private ModelMultipleLights makeSphere(GL3 gl) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "shaders/vs_sphere_01.txt", "shaders/fs_standard_1t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    ModelMultipleLights sphere = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t0, t1);
    return sphere;
  } 

  private SGNode makeSecurityPost(GL3 gl) {
    sphere = makeSphere(gl);
    securityRoot = new NameNode("securitylightroot");
    securityMoveTranslate = new TransformNode("securitylight transform", Mat4Transform.translate(xPosition-5f,0,5f));

    NameNode securityPost = new NameNode("securitylightpost");
    Mat4 m = Mat4Transform.scale(0.5f,10f,0.5f);
      m = Mat4.multiply(m,Mat4Transform.translate(0,0.5f,0));
      TransformNode securityPostTransform = new TransformNode("securitypost transform", m);
        ModelNode securityPostShape = new ModelNode("Sphere(securityPost)", sphere);
    
    NameNode securityTop = new NameNode("securitylighttop");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0,10f,0));
      TransformNode securityTopTranslate = new TransformNode("securitytop translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(2f,0.5f,0.5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode securityTopTransform = new TransformNode("securitytop transform", m);
        ModelNode securityTopShape = new ModelNode("Sphere(securitytop)",sphere);

    rotateTop = new TransformNode("rotateAroundY("+rotateTopAngle+")", Mat4Transform.rotateAroundY(rotateTopAngle));

    securityRoot.addChild(securityMoveTranslate);
      securityMoveTranslate.addChild(securityPost);
        securityPost.addChild(securityPostTransform);
          securityPostTransform.addChild(securityPostShape);
        securityPost.addChild(securityTopTranslate);
          securityTopTranslate.addChild(rotateTop);
            rotateTop.addChild(securityTop);
              securityTop.addChild(securityTopTransform);
                securityTopTransform.addChild(securityTopShape);
    
    securityRoot.update();
    return securityRoot;
  }

  public void render(GL3 gl) {
    double elapsedTime = getSeconds() - startTime;
    rotateTopAngle = 30*(float)Math.sin(elapsedTime);
    rotateTop.setTransform(Mat4Transform.rotateAroundY(rotateTopAngle));
    securityRoot.update();
    securityRoot.draw(gl);
  }

  public void dispose(GL3 gl) {
    sphere.dispose(gl);
  }

  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
}