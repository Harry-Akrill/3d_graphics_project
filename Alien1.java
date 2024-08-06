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

public class Alien1 {
  
  private ModelMultipleLights sphere, arm_sphere, head_sphere;
  private Camera camera;
  private Light[] lights;
  private Texture t0, t1, t2, t3, t4, t5;
  private float size = 16f;

  // ***************************************************
  /*
   * Scene graph + animation variable declarations
   */
  private SGNode alien1Root,alien;
  private TransformNode alien1BodyTransform,alien1MoveTranslate,alien1HeadTranslate,rotateHead,rotateBody;

  private float xPosition = 0;
  private float rotateHead1AngleStart = 0, rotateHead1Angle = rotateHead1AngleStart;
  private float rotateBodyAngleStart = 0, rotateBodyAngle = rotateBodyAngleStart;

  private int rockingHeadType = 0;
  private int rockingBodyType = 0;

  public Alien1(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5) {
    camera = c;
    lights = l;
    this.t0 = t0;
    this.t1 = t1;
    this.t2 = t2;
    this.t3 = t3;
    this.t4 = t4;
    this.t5 = t5;
    alien = makeAlien1(gl);
    startTime = getSeconds();
  }
  // makeSphere from labs
  private ModelMultipleLights makeSphere(GL3 gl, Texture t0, Texture t1) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "shaders/vs_sphere_01.txt", "shaders/fs_alien_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    ModelMultipleLights sphere = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t0, t1);
    return sphere;
  } 

  private SGNode makeAlien1(GL3 gl) {
    sphere = makeSphere(gl,t0,t1);
    arm_sphere = makeSphere(gl,t2,t3);
    head_sphere = makeSphere(gl,t4,t5);

    // Declaring common variables for added clarity
    float bodyWidth = 1.8f;
    float bodyScale = 2;
    float bodyLength = 1.8f;
    float bodyDepth = 1f;

    float headWidth = 1.3f;
    float headLength = 1.3f;
    float headDepth = 1f;
    float headScale = 0.6f;
    float headHeight = 1.5f;

    float armLength = 1.2f;
    float armWidth = 0.4f;
    float armDepth = 0.4f;
    float armHeight = 0.9f;

    float eyeHeight = 0.8f;
    float eyeDepth = 0.1f;
    float eyeScale = 0.2f;

    float earHeight = 0.4f;
    float earDepth = 0.3f;
    float earLength = 0.8f;
    float earWidth = 0.1f;

    alien1Root = new NameNode("alien1Root");
    alien1MoveTranslate = new TransformNode("alien1 transform",Mat4Transform.translate(xPosition - 3f,0,0));

    NameNode alien1Body = new NameNode("alien1body");
      Mat4 m = Mat4Transform.scale(bodyWidth,bodyLength,bodyDepth);
      m = Mat4.multiply(m,Mat4Transform.translate(0,0.5f,0));
      TransformNode alien1BodyTransform = new TransformNode("alien1body transform", m);
        ModelNode alien1BodyShape = new ModelNode("Sphere(alien1body)",sphere);

    rotateBody = new TransformNode("rotateAroundX("+rotateBodyAngle+")", Mat4Transform.rotateAroundX(rotateBodyAngle));
    
    NameNode alien1Arm1 = new NameNode("alien1arm1");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(-1.3f,armHeight,0));
      TransformNode alien1Arm1Translate = new TransformNode("alien1arm1 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armLength,armWidth,armDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0));
      TransformNode alien1Arm1Transform = new TransformNode("alien1arm1 transform", m);
        ModelNode alien1Arm1Shape = new ModelNode("Sphere(alien1arm1)", arm_sphere);
    
    NameNode alien1Arm2 = new NameNode("alien1arm2");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(1.3f,armHeight,0));
      TransformNode alien1Arm2Translate = new TransformNode("alien1arm2 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armLength,armWidth,armDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0));
      TransformNode alien1Arm2Transform = new TransformNode("alien1arm2 transform", m);
        ModelNode alien1Arm2Shape = new ModelNode("Sphere(alien1arm2)", arm_sphere);

    NameNode alien1Head = new NameNode("alien1head");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0f,(headHeight + 0.1f),0));
      TransformNode alien1HeadTranslate = new TransformNode("alien1head translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(headWidth,headLength,headDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien1HeadTransform = new TransformNode("alien1head transform", m);
        ModelNode alien1HeadShape = new ModelNode("Sphere(alien1head)",head_sphere);

    rotateHead = new TransformNode("rotateAroundZ("+rotateHead1Angle+")", Mat4Transform.rotateAroundZ(rotateHead1Angle));

    NameNode alien1Eye1 = new NameNode("alien1eye1");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(-0.2f, eyeHeight, 0.4f));
      TransformNode alien1Eye1Translate = new TransformNode("alien1eye1 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(eyeScale,eyeScale,eyeDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien1Eye1Transform = new TransformNode("alien1eye1 transform", m);
        ModelNode alien1Eye1Shape = new ModelNode("Sphere(alien1eye1)",sphere);

    NameNode alien1Eye2 = new NameNode("alien1eye2");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0.2f, eyeHeight, 0.4f));
      TransformNode alien1Eye2Translate = new TransformNode("alien1eye2 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(eyeScale,eyeScale,eyeDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien1Eye2Transform = new TransformNode("alien1eye2 transform", m);
        ModelNode alien1Eye2Shape = new ModelNode("Sphere(alien1eye2)", sphere);

    NameNode alien1Ear1 = new NameNode("alien1ear1");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(-0.65f,earHeight, 0f));
      TransformNode alien1Ear1Translate = new TransformNode("alien1ear1 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(earWidth,earLength,earDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien1Ear1Transform = new TransformNode("alien1ear1 transform", m);
        ModelNode alien1Ear1Shape = new ModelNode("Sphere(alien1ear1)", sphere);

    NameNode alien1Ear2 = new NameNode("alien1ear2");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0.65f,earHeight, 0f));
      TransformNode alien1Ear2Translate = new TransformNode("alien1ear2 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(earWidth,earLength,earDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien1Ear2Transform = new TransformNode("alien1ear2 transform", m);
        ModelNode alien1Ear2Shape = new ModelNode("Sphere(alien1ear2)", sphere);

    NameNode alien1AntennaStick = new NameNode("alien1antennastick");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0f,1f,0f));
      TransformNode alien1AntennaStickTranslate = new TransformNode("alien1antennastick translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.1f,0.6f,0.1f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien1AntennaStickTransform = new TransformNode("alien1antennastick transform", m);
        ModelNode alien1AntennaStickShape = new ModelNode("Sphere(alien1antennastick)", sphere);

    NameNode alien1AntennaBall = new NameNode("alien1antennaball");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.6f,0f));
      TransformNode alien1AntennaBallTranslate = new TransformNode("alien1antennaball translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.2f,0.2f,0.2f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien1AntennaBallTransform = new TransformNode("alien1antennaball transform", m);
        ModelNode alien1AntennaBallShape = new ModelNode("Sphere(alien1antennaball)", sphere);

    // Scene graph for alien 1
    alien1Root.addChild(alien1MoveTranslate);
    alien1MoveTranslate.addChild(rotateBody);
      rotateBody.addChild(alien1Body);
        alien1Body.addChild(alien1BodyTransform);
          alien1BodyTransform.addChild(alien1BodyShape);
        alien1Body.addChild(alien1Arm1Translate);
          alien1Arm1Translate.addChild(alien1Arm1);
            alien1Arm1.addChild(alien1Arm1Transform);
              alien1Arm1Transform.addChild(alien1Arm1Shape);

        alien1Body.addChild(alien1Arm2Translate);
          alien1Arm2Translate.addChild(alien1Arm2);
            alien1Arm2.addChild(alien1Arm2Transform);
              alien1Arm2Transform.addChild(alien1Arm2Shape);

        alien1Body.addChild(alien1HeadTranslate);
          alien1HeadTranslate.addChild(rotateHead);
            rotateHead.addChild(alien1Head);
              alien1Head.addChild(alien1HeadTransform);
                alien1HeadTransform.addChild(alien1HeadShape);
              alien1Head.addChild(alien1Eye1Translate);
                alien1Eye1Translate.addChild(alien1Eye1);
                  alien1Eye1.addChild(alien1Eye1Transform);
                    alien1Eye1Transform.addChild(alien1Eye1Shape);
              alien1Head.addChild(alien1Eye2Translate);
                alien1Eye2Translate.addChild(alien1Eye2);
                  alien1Eye2.addChild(alien1Eye2Transform);
                    alien1Eye2Transform.addChild(alien1Eye2Shape);
              alien1Head.addChild(alien1Ear1Translate);
                alien1Ear1Translate.addChild(alien1Ear1);
                  alien1Ear1.addChild(alien1Ear1Transform);
                    alien1Ear1Transform.addChild(alien1Ear1Shape);
              alien1Head.addChild(alien1Ear2Translate);
                alien1Ear2Translate.addChild(alien1Ear2);
                  alien1Ear2.addChild(alien1Ear2Transform);
                    alien1Ear2Transform.addChild(alien1Ear2Shape);
              alien1Head.addChild(alien1AntennaStickTranslate);
                alien1AntennaStickTranslate.addChild(alien1AntennaStick);
                  alien1AntennaStick.addChild(alien1AntennaStickTransform);
                    alien1AntennaStickTransform.addChild(alien1AntennaStickShape);
                  alien1AntennaStick.addChild(alien1AntennaBallTranslate);
                    alien1AntennaBallTranslate.addChild(alien1AntennaBall);
                      alien1AntennaBall.addChild(alien1AntennaBallTransform);
                        alien1AntennaBallTransform.addChild(alien1AntennaBallShape);
                    
            

    alien1Root.update();
    //alien1Root.print(0,false);
    //System.exit(0);
    return alien1Root;
  }

  public void render(GL3 gl) {
    // Animation for alien
    if (rockingHeadType == 1) {
      rockHeadSide();
    }
    else if (rockingHeadType == 2) {
      rockHeadForwards();
    }
    else if (rockingHeadType == 3) {
      rockHeadAround();
    }

    if (rockingBodyType == 1) {
      rockBodyForwards();
    }
    else if (rockingBodyType == 2) {
      rockBodySide();
    }
    else if (rockingBodyType == 3) {
      rockBodyAround();
    }
    alien.draw(gl);
  }

  // ***************************************************
  /* ALIEN BODY PART ANIMATION
   *
   */
  public void rockBodyForwards() {
    double elapsedTime = getSeconds() - startTime;
    rotateBodyAngle = 30*(float)Math.sin(elapsedTime);
    rotateBody.setTransform(Mat4Transform.rotateAroundX(rotateBodyAngle));
    alien1Root.update();
  }

  public void rockBodySide() {
    double elapsedTime = getSeconds() - startTime;
    rotateBodyAngle = 30*(float)Math.sin(elapsedTime);
    rotateBody.setTransform(Mat4Transform.rotateAroundZ(rotateBodyAngle));
    alien1Root.update();
  }

  public void rockBodyAround() {
    double elapsedTime = getSeconds() - startTime;
    float rotateBodyXAngle = 20*(float)Math.sin(1.5*elapsedTime);
    float rotateBodyYAngle = 30*(float)Math.cos(elapsedTime);
    float rotateBodyZAngle = 15*(float)Math.sin(2*elapsedTime);
    Mat4 rotateXTransform = Mat4Transform.rotateAroundX(rotateBodyXAngle);
    Mat4 rotateYTransform = Mat4Transform.rotateAroundY(rotateBodyYAngle);
    Mat4 rotateZTransform = Mat4Transform.rotateAroundZ(rotateBodyZAngle);
    Mat4 combinedRotation = Mat4.multiply(Mat4.multiply(rotateXTransform, rotateYTransform), rotateZTransform);
    rotateBody.setTransform(combinedRotation);
    alien1Root.update();
  }

  public void rockHeadSide() {
    double elapsedTime = getSeconds() - startTime;
    rotateHead1Angle = 30*(float)Math.sin(elapsedTime);
    rotateHead.setTransform(Mat4Transform.rotateAroundZ(rotateHead1Angle));
    alien1Root.update();
  }

  public void rockHeadForwards() {
    double elapsedTime = getSeconds() - startTime;
    rotateHead1Angle = 30*(float)Math.sin(elapsedTime);
    rotateHead.setTransform(Mat4Transform.rotateAroundX(rotateHead1Angle));
    alien1Root.update();
  }
  
  public void rockHeadAround() {
    double elapsedTime = getSeconds() - startTime;
    float rotateHead1XAngle = 30*(float)Math.sin(elapsedTime);
    float rotateHead1YAngle = 45*(float)Math.cos(elapsedTime);
    Mat4 rotateXTransform = Mat4Transform.rotateAroundX(rotateHead1XAngle);
    Mat4 rotateYTransform = Mat4Transform.rotateAroundY(rotateHead1YAngle);
    Mat4 combinedRotation = Mat4.multiply(rotateXTransform, rotateYTransform);
    rotateHead.setTransform(combinedRotation);
    alien1Root.update();
  }

  public void updateAlienHeadForward() {
    rockingHeadType = 1;
  }

  public void updateAlienHeadSide() {
    rockingHeadType = 2;
  }

  public void updateAlienHeadAround() {
    rockingHeadType = 3;
  }

  public void updateAlienHeadStop() {
    rockingHeadType = 0;
  }

  public void updateAlienBodyForward() {
    rockingBodyType = 1;
  }
  
  public void updateAlienBodySide() {
    rockingBodyType = 2;
  }

  public void updateAlienBodyAround() {
    rockingBodyType = 3;
  }

  public void updateAlienBodyStop() {
    rockingBodyType = 0;
  }

  public void dispose(GL3 gl) {
    sphere.dispose(gl);
  }

  // ***************************************************
  /* TIME
   */
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
}