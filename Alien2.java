/* I declare that this code is my own work, outside of the work implemented from the lab classes, mainly ch8 and the scene graph work in ch7 */
/* Author Harry Akrill hakrill1@sheffield.ac.uk */
import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Alien2 {
  
  private ModelMultipleLights sphere,arm_sphere,head_sphere;
  private Camera camera;
  private Light[] lights;
  private Texture t0, t1, t2, t3, t4, t5;
  private float size = 16f;

  // ***************************************************
  /*
   * Scene graph + animation variable declarations
   */
  private SGNode alien2Root,alien;
  private TransformNode alien2BodyTransform,alien2MoveTranslate,alien2HeadTranslate,rotateHead,rotateBody;

  private float xPosition = 0;
  private float rotateHead1AngleStart = 0, rotateHead1Angle = rotateHead1AngleStart;
  private float rotateBodyAngleStart = 0, rotateBodyAngle = rotateBodyAngleStart;

  private int rockingHeadType = 0;
  private int rockingBodyType = 0;

  public Alien2(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5) {
    camera = c;
    lights = l;
    this.t0 = t0;
    this.t1 = t1;
    this.t2 = t2;
    this.t3 = t3;
    this.t4 = t4;
    this.t5 = t5;
    alien = makeAlien2(gl);
    startTime = getSeconds();
  }

  private ModelMultipleLights makeSphere(GL3 gl, Texture t0, Texture t1) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "shaders/vs_sphere_01.txt", "shaders/fs_alien_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    ModelMultipleLights sphere = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t0, t1);
    return sphere;
  } 

  private SGNode makeAlien2(GL3 gl) {
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

    alien2Root = new NameNode("alien2Root");
    alien2MoveTranslate = new TransformNode("alien2 transform",Mat4Transform.translate(xPosition + 3f,0,0));

    NameNode alien2Body = new NameNode("alien2body");
      Mat4 m = Mat4Transform.scale(bodyWidth,bodyLength,bodyDepth);
      m = Mat4.multiply(m,Mat4Transform.translate(0,0.5f,0));
      TransformNode alien2BodyTransform = new TransformNode("alien2body transform", m);
        ModelNode alien2BodyShape = new ModelNode("Sphere(alien2body)",sphere);

    rotateBody = new TransformNode("rotateAroundX("+rotateBodyAngle+")", Mat4Transform.rotateAroundX(rotateBodyAngle));

    NameNode alien2Arm1 = new NameNode("alien2arm1");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(-1.3f,armHeight,0));
      TransformNode alien2Arm1Translate = new TransformNode("alien2arm1 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armLength,armWidth,armDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0));
      TransformNode alien2Arm1Transform = new TransformNode("alien2arm1 transform", m);
        ModelNode alien2Arm1Shape = new ModelNode("Sphere(alien2arm1)", arm_sphere);
    
    NameNode alien2Arm2 = new NameNode("alien2arm2");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(1.3f,armHeight,0));
      TransformNode alien2Arm2Translate = new TransformNode("alien2arm2 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armLength,armWidth,armDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0));
      TransformNode alien2Arm2Transform = new TransformNode("alien2arm2 transform", m);
        ModelNode alien2Arm2Shape = new ModelNode("Sphere(alien2arm2)", arm_sphere);

    NameNode alien2Head = new NameNode("alien2head");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0f,(headHeight + 0.1f),0));
      TransformNode alien2HeadTranslate = new TransformNode("alien2head translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(headWidth,headLength,headDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien2HeadTransform = new TransformNode("alien2head transform", m);
        ModelNode alien2HeadShape = new ModelNode("Sphere(alien2head)",head_sphere);

    rotateHead = new TransformNode("rotateAroundZ("+rotateHead1Angle+")", Mat4Transform.rotateAroundZ(rotateHead1Angle));

    NameNode alien2Eye1 = new NameNode("alien2eye1");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(-0.2f, eyeHeight, 0.4f));
      TransformNode alien2Eye1Translate = new TransformNode("alien2eye1 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(eyeScale,eyeScale,eyeDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien2Eye1Transform = new TransformNode("alien2eye1 transform", m);
        ModelNode alien2Eye1Shape = new ModelNode("Sphere(alien2eye1)",sphere);

    NameNode alien2Eye2 = new NameNode("alien2eye2");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0.2f, eyeHeight, 0.4f));
      TransformNode alien2Eye2Translate = new TransformNode("alien2eye2 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(eyeScale,eyeScale,eyeDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien2Eye2Transform = new TransformNode("alien2eye2 transform", m);
        ModelNode alien2Eye2Shape = new ModelNode("Sphere(alien2eye2)", sphere);

    NameNode alien2Ear1 = new NameNode("alien2ear1");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(-0.65f,earHeight, 0f));
      TransformNode alien2Ear1Translate = new TransformNode("alien2ear1 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(earWidth,earLength,earDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien2Ear1Transform = new TransformNode("alien2ear1 transform", m);
        ModelNode alien2Ear1Shape = new ModelNode("Sphere(alien2ear1)", sphere);

    NameNode alien2Ear2 = new NameNode("alien2ear2");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0.65f,earHeight, 0f));
      TransformNode alien2Ear2Translate = new TransformNode("alien2ear2 translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(earWidth,earLength,earDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien2Ear2Transform = new TransformNode("alien2ear2 transform", m);
        ModelNode alien2Ear2Shape = new ModelNode("Sphere(alien2ear2)", sphere);

    NameNode alien2AntennaStick = new NameNode("alien2antennastick");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0f,1f,0f));
      TransformNode alien2AntennaStickTranslate = new TransformNode("alien2antennastick translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.1f,0.6f,0.1f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien2AntennaStickTransform = new TransformNode("alien2antennastick transform", m);
        ModelNode alien2AntennaStickShape = new ModelNode("Sphere(alien2antennastick)", sphere);

    NameNode alien2AntennaBall = new NameNode("alien2antennaball");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.6f,0f));
      TransformNode alien2AntennaBallTranslate = new TransformNode("alien2antennaball translate", m);

      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.2f,0.2f,0.2f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0f));

      TransformNode alien2AntennaBallTransform = new TransformNode("alien2antennaball transform", m);
        ModelNode alien2AntennaBallShape = new ModelNode("Sphere(alien2antennaball)", sphere);

    // Scene graph for alien 2
    alien2Root.addChild(alien2MoveTranslate);
    alien2MoveTranslate.addChild(rotateBody);
      rotateBody.addChild(alien2Body);
        alien2Body.addChild(alien2BodyTransform);
          alien2BodyTransform.addChild(alien2BodyShape);
        alien2Body.addChild(alien2Arm1Translate);
          alien2Arm1Translate.addChild(alien2Arm1);
            alien2Arm1.addChild(alien2Arm1Transform);
              alien2Arm1Transform.addChild(alien2Arm1Shape);

        alien2Body.addChild(alien2Arm2Translate);
          alien2Arm2Translate.addChild(alien2Arm2);
            alien2Arm2.addChild(alien2Arm2Transform);
              alien2Arm2Transform.addChild(alien2Arm2Shape);

        alien2Body.addChild(alien2HeadTranslate);
          alien2HeadTranslate.addChild(rotateHead);
            rotateHead.addChild(alien2Head);
              alien2Head.addChild(alien2HeadTransform);
                alien2HeadTransform.addChild(alien2HeadShape);
              alien2Head.addChild(alien2Eye1Translate);
                alien2Eye1Translate.addChild(alien2Eye1);
                  alien2Eye1.addChild(alien2Eye1Transform);
                    alien2Eye1Transform.addChild(alien2Eye1Shape);
              alien2Head.addChild(alien2Eye2Translate);
                alien2Eye2Translate.addChild(alien2Eye2);
                  alien2Eye2.addChild(alien2Eye2Transform);
                    alien2Eye2Transform.addChild(alien2Eye2Shape);
              alien2Head.addChild(alien2Ear1Translate);
                alien2Ear1Translate.addChild(alien2Ear1);
                  alien2Ear1.addChild(alien2Ear1Transform);
                    alien2Ear1Transform.addChild(alien2Ear1Shape);
              alien2Head.addChild(alien2Ear2Translate);
                alien2Ear2Translate.addChild(alien2Ear2);
                  alien2Ear2.addChild(alien2Ear2Transform);
                    alien2Ear2Transform.addChild(alien2Ear2Shape);
              alien2Head.addChild(alien2AntennaStickTranslate);
                alien2AntennaStickTranslate.addChild(alien2AntennaStick);
                  alien2AntennaStick.addChild(alien2AntennaStickTransform);
                    alien2AntennaStickTransform.addChild(alien2AntennaStickShape);
                  alien2AntennaStick.addChild(alien2AntennaBallTranslate);
                    alien2AntennaBallTranslate.addChild(alien2AntennaBall);
                      alien2AntennaBall.addChild(alien2AntennaBallTransform);
                        alien2AntennaBallTransform.addChild(alien2AntennaBallShape);
                  
            

    alien2Root.update();
    //alien2Root.print(0,false);
    //System.exit(0);
    return alien2Root;
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
    alien2Root.update();
  }

  public void rockBodySide() {
    double elapsedTime = getSeconds() - startTime;
    rotateBodyAngle = 30*(float)Math.sin(elapsedTime);
    rotateBody.setTransform(Mat4Transform.rotateAroundZ(rotateBodyAngle));
    alien2Root.update();
  }

  public void rockBodyAround() {
    double elapsedTime = getSeconds() - startTime;
    float rotateBodyXAngle = 20*(float)Math.sin(elapsedTime);
    float rotateBodyYAngle = 30*(float)Math.cos(1.2*elapsedTime);
    float rotateBodyZAngle = 15*(float)Math.sin(2*elapsedTime);
    Mat4 rotateXTransform = Mat4Transform.rotateAroundX(rotateBodyXAngle);
    Mat4 rotateYTransform = Mat4Transform.rotateAroundY(rotateBodyYAngle);
    Mat4 rotateZTransform = Mat4Transform.rotateAroundZ(rotateBodyZAngle);
    Mat4 combinedRotation = Mat4.multiply(Mat4.multiply(rotateXTransform, rotateYTransform), rotateZTransform);
    rotateBody.setTransform(combinedRotation);
    alien2Root.update();
  }

  public void rockHeadSide() {
    double elapsedTime = getSeconds() - startTime;
    rotateHead1Angle = 30*(float)Math.sin(elapsedTime);
    rotateHead.setTransform(Mat4Transform.rotateAroundZ(rotateHead1Angle));
    alien2Root.update();
  }

  public void rockHeadForwards() {
    double elapsedTime = getSeconds() - startTime;
    rotateHead1Angle = 30*(float)Math.sin(elapsedTime);
    rotateHead.setTransform(Mat4Transform.rotateAroundX(rotateHead1Angle));
    alien2Root.update();
  }
  
  public void rockHeadAround() {
    double elapsedTime = getSeconds() - startTime;
    float rotateHead1XAngle = 20*(float) Math.sin(1.5*elapsedTime);
    float rotateHead1YAngle = 45*(float) Math.cos(2*elapsedTime);
    float rotateHeadTwistAngle = 10*(float) Math.sin(3*elapsedTime);
    Mat4 rotateXTransform = Mat4Transform.rotateAroundX(rotateHead1XAngle);
    Mat4 rotateYTransform = Mat4Transform.rotateAroundY(rotateHead1YAngle);
    Mat4 rotateTwistTransform = Mat4Transform.rotateAroundZ(rotateHeadTwistAngle);
    Mat4 combinedRotation = Mat4.multiply(Mat4.multiply(rotateXTransform, rotateYTransform), rotateTwistTransform);
    rotateHead.setTransform(combinedRotation);
    alien2Root.update();
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