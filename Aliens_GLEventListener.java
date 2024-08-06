/* I declare that this code is my own work, outside of the work implemented from the lab classes, mainly ch8 */
/* Author Harry Akrill hakrill1@sheffield.ac.uk */
import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;
  
public class Aliens_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
    
  /* The constructor is not used to initialise anything */
  public Aliens_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,6f,15f));
    this.camera.setTarget(new Vec3(0f,5f,0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled' so needs to be enabled
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    alien1.dispose(gl);
    backdrop.dispose(gl);
    alien2.dispose(gl);
    securityPost.dispose(gl);
    lights[0].dispose(gl);
    lights[1].dispose(gl);
    //securityLight.dispose(gl);
  }

  // ***************************************************
  /* THE SCENE
   * All the methods used within the scene are handled here.
   */

  // textures
  private TextureLibrary textures;

  private Backdrop backdrop;
  private Alien1 alien1;
  private Alien2 alien2;
  private SecurityPost securityPost;
  private SecurityLight securityLight;
  private Light[] lights = new Light[2];
  //private Vec3 spotlightDirection = new Vec3(0f, -1f, 0f); not used as in security light

  private void loadTextures(GL3 gl) {
    textures = new TextureLibrary();
    textures.add(gl, "background", "textures/background.jpg");
    textures.add(gl, "snowfloor", "textures/snowfloor.jpg");
    textures.add(gl, "snowfalling", "textures/snowfalling.jpg");
    textures.add(gl, "blackbackground", "textures/blackbackground.jpg");
    textures.add(gl, "sidewall", "textures/sidewall.jpg");
    textures.add(gl, "alientexture", "textures/alientexture.jpg");
    textures.add(gl, "alien_specular", "textures/alientexture_specular.jpg");
    textures.add(gl, "alien_arm", "textures/alienarmtexture.jpg");
    textures.add(gl, "alien_arm_specular", "textures/alienarmtexture_specular.jpg");
    textures.add(gl, "alien_head", "textures/alienheadtexture.jpg");
    textures.add(gl, "alien_head_specular", "textures/alienheadtexture_specular.jpg");
    textures.add(gl, "alien2texture", "textures/alien2texture.jpg");
    textures.add(gl, "alien2_specular", "textures/alien2texture_specular.jpg");
    textures.add(gl, "alien2_arm", "textures/alien2armtexture.jpg");
    textures.add(gl, "alien2_arm_specular", "textures/alien2armtexture_specular.jpg");
    textures.add(gl, "alien2_head", "textures/alien2headtexture.jpg");
    textures.add(gl, "alien2_head_specular", "textures/alien2headtexture_specular.jpg");
    textures.add(gl, "security_post", "textures/securitypost.jpg");
    textures.add(gl, "security_post_specular", "textures/securitypost_specular.jpg");
  }

  public void initialise(GL3 gl) {
    loadTextures(gl);

    lights[0] = new Light(gl);
    lights[0].setCamera(camera);
    lights[1] = new Light(gl);
    lights[1].setCamera(camera);

    backdrop = new Backdrop(gl, camera, lights, textures.get("snowfloor"), textures.get("background"), textures.get("snowfalling"), textures.get("blackbackground"),
    textures.get("sidewall"));
    alien1 = new Alien1(gl, camera, lights, textures.get("alientexture"), textures.get("alien_specular"), textures.get("alien_arm"), textures.get("alien_arm_specular"),
    textures.get("alien_head"), textures.get("alien_head_specular"));
    alien2 = new Alien2(gl, camera, lights, textures.get("alien2texture"), textures.get("alien2_specular"), textures.get("alien2_arm"), textures.get("alien2_arm_specular"),
    textures.get("alien2_head"), textures.get("alien2_head_specular"));  
    securityPost = new SecurityPost(gl,camera,lights,textures.get("security_post"),textures.get("security_post_specular"));
    //securityLight = new SecurityLight(gl);
    //securityLight.setCamera(camera);
    //securityLight.setPosition(new Vec3(-5f, 10f, 5f));
    //securityLight.setSpotlightDirection(new Vec3(0f, -1f, 0f));
  }

  // *************************************
  /*
   * Light switches
   */
  private boolean light1Switch = true;
  private boolean light2Switch = true;

  public void light1Switch() {
    if (light1Switch == true){
      light1Switch = false;
    }

    else {
      light1Switch = true;
    }
  }

  public void light2Switch() {
    if (light2Switch) {
      light2Switch = false;
    }
    else {
      light2Switch = true;
    }
  }
  
  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    double elapsedTime = getSeconds() - startTime;
    lights[0].setPosition(-7.5f,16f,-7.5f); 
    lights[1].setPosition(7.5f,16f,7.5f); 
    
    // set texture of material if lights are on / off
    if (light1Switch == true){
      Material material = new Material();
      material.setAmbient(0.5f, 0.5f, 0.5f);
      material.setDiffuse(0.7f, 0.7f, 0.7f);
      material.setSpecular(0.7f, 0.7f, 0.7f);

      lights[0].setMaterial(material);
      lights[0].render(gl);

    }
    else{
      Material material = new Material();
      material.setAmbient(0.1f, 0.1f, 0.1f);
      material.setDiffuse(0f, 0f, 0f);
      material.setSpecular(0f, 0f, 0f);
      lights[0].setMaterial(material);
      lights[0].render(gl);
    }

    if (light2Switch == true){
      Material material = new Material();
      material.setAmbient(0.5f, 0.5f, 0.5f);
      material.setDiffuse(0.7f, 0.7f, 0.7f);
      material.setSpecular(0.7f, 0.7f, 0.7f);

      lights[1].setMaterial(material);
      lights[1].render(gl);
    }
    else{
      Material material = new Material();
      material.setAmbient(0.1f, 0.1f, 0.1f);
      material.setDiffuse(0f, 0f, 0f);
      material.setSpecular(0f, 0f, 0f);
      lights[1].setMaterial(material);
      lights[1].render(gl);
    }
    
    backdrop.render(gl, elapsedTime);
    alien1.render(gl);
    alien2.render(gl);
    securityPost.render(gl);
    //securityLight.update(elapsedTime);
    //securityLight.render(gl);
  }
  // ***************************************************
  /*
   * Alien animation updaters
   */
  public void updateAlienHeadForward() {
    alien1.updateAlienHeadForward();
    alien2.updateAlienHeadForward();
  }

  public void updateAlienHeadSide() {
    alien1.updateAlienHeadSide();
    alien2.updateAlienHeadSide();
  }

  public void updateAlienHeadAround() {
    alien1.updateAlienHeadAround();
    alien2.updateAlienHeadAround();
  }

  public void updateAlienHeadStop() {
    alien1.updateAlienHeadStop();
    alien2.updateAlienHeadStop();
  }

  public void updateAlienBodyForward() {
    alien1.updateAlienBodyForward();
    alien2.updateAlienBodyForward();
  }

  public void updateAlienBodySide() {
    alien1.updateAlienBodySide();
    alien2.updateAlienBodySide();
  }

  public void updateAlienBodyAround() {
    alien1.updateAlienBodyAround();
    alien2.updateAlienBodyAround();
  }

  public void updateAlienBodyStop() {
    alien1.updateAlienBodyStop();
    alien2.updateAlienBodyStop();
  }
  
    // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
}