package com.lxm.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BoxWorld {

	private static Fixture Fixture;
	private static Filter FilterData;

	// 创建一个世界，用来维护运动，0 -20的意思为x和y方向的加速度，true为允许停止模拟已经不运行的物体
	public static World createPhysicsWorld() {
		// we instantiate a new World with a proper gravity vector
		// and tell it to sleep when possible.
		return new World(new Vector2(0f, -30f), true);
	}

	// 参数解释：world ， 盒子hx，盒子hy ，box类别：StaticBody(0), KinematicBody(1),
	// DynamicBody(2)
	// 位置 pos_x pos_y 质量 density
	//categoryBits  分组    maskBits 与哪些组碰撞检测
	public static Body createBoxes(World world, float hx, float hy,
			BodyDef.BodyType type, float pos_x, float pos_y, float density,
			short categoryBits, short maskBits,boolean sensor) {

		// 形状，设置为盒子，四方形，hx hy为一半的长度。真正长度*2
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(hx, hy);
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = type;
		boxBodyDef.position.x = pos_x;
		boxBodyDef.position.y = pos_y;
		Body boxBody = world.createBody(boxBodyDef);
		// 如果需要摩擦力，阻力 ，创建自定义夹具，接收下面的参数，然后修改夹具参数
		Fixture = boxBody.createFixture(boxPoly, density);
		// 做的不好了
		FilterData = Fixture.getFilterData();
		FilterData.categoryBits = categoryBits;
		FilterData.maskBits = maskBits;
		Fixture.setSensor(sensor);
		Fixture.setFilterData(FilterData);
		// we are done, all that's left is disposing the boxPoly
		boxPoly.dispose();
		return boxBody;
	}

	/*
	 * 
	 * 除了积分器以外，Box2D还使用了大量的位代码来调用约束求解器(constraint
	 * solver)。约束求解器每次解决一个约束，解决了模拟过程中所有的约束问题
	 * 。单个约束可以被完美的解决。尽管如此，每当我们解决了一个约束，我们又轻易的扰乱了其他约束。为了解决所有问题，我们需要多次遍历所有约束。
	 * 在约束求解器(constraint
	 * solver)中有两个阶段:速度阶段和位置阶段。在速度阶段，求解器为使物体能够正确的移动需要计算必要的冲量。在位置阶段
	 * ，求解器调整物体的位置来降低物体之间的重叠和连接的分离程度
	 * (译者注:比如两个单独的物体碰撞完之后会有瞬间重叠的情况，还有多节点物体在碰撞完之后会有瞬间的分离的情况
	 * ，所以会有重叠和分离两种情况)。每一个阶段都有其自身需要的迭代次数。另外，在位置阶段如果有误差足够小，可能会提早退出迭代。
	 * Box2D建议的迭代次数是速度阶段8次
	 * ，位置阶段3次。你可以根据你的喜好改变这个数字，但是要记住，这需要在效率和精度上做一个权衡。用更少的迭代次数会增加提升效率
	 * ，但是会影响精度。同样，用更多的迭代次数会降低效率但会提升模拟质量
	 * ，会产生更高的精度。对于这个简单的例子而言，我们不需要太多的迭代次数，下面是我们选择的迭代次数。
	 */
	public static void renden(World world) {
		world.step(Gdx.graphics.getDeltaTime(), 8, 3);
	}

	// box的pos位置为中心点位置，比如16*16的，设置位置8*8，则在中心点上。

	// boby接口
	/*
	 * setLinearVelocity 设置线性速度 setBullet 设置成子弹，则会对其轨迹进行事先判断，防止隧道穿越出现
	 * setLinearDamping 设直线型阻尼 applyLinearImpulse 应用一个冲量 setAngularVelocity
	 * 設置角速度
	 */
	// 创建关节的套路如下：
	/*
	 * //创建一个距离关节 DistanceJointDef distanceJointDef = new DistanceJointDef();//
	 * 使用关节定义，初始化数据 distanceJointDef.bodyA = mCircle1; distanceJointDef.bodyB =
	 * mCircle2; //设置长度为4米 distanceJointDef.length = 4f;
	 * distanceJointDef.collideConnected = true;// 是否需要检测两个物体是否碰撞
	 * mWorld.createJoint(distanceJointDef);// 创建关节
	 */
	/*
	 * 旋转关节 RevoluteJointDef jd = new RevoluteJointDef(); jd.collideConnected =
	 * false; jd.initialize(prevBody, body, anchor);// 参数，两个刚体，一个锚点
	 * world.createJoint(jd);
	 */

	/*
	 * 鼠标关节的代码，点击之后生成一个鼠标关节 // 接受查询结果 QueryCallback callback = new
	 * QueryCallback() {
	 * 
	 * @Override public boolean reportFixture (Fixture fixture) { // if the hit
	 * fixture's body is the ground body // we ignore it if (fixture.getBody()
	 * == groundBody) return true;
	 * 
	 * // if the hit point is inside the fixture of the body // we report it if
	 * (fixture.testPoint(testPoint.x, testPoint.y)) { hitBody =
	 * fixture.getBody(); return false; } else return true; } };
	 * 
	 * @Override public boolean touchDown (int x, int y, int pointer, int
	 * newParam) { // translate the mouse coordinates to world coordinates
	 * testPoint.set(x, y, 0); camera.unproject(testPoint);
	 * 
	 * // ask the world which bodies are within the given // bounding box around
	 * the mouse pointer hitBody = null; world.QueryAABB(callback, testPoint.x -
	 * 0.1f, testPoint.y - 0.1f, testPoint.x + 0.1f, testPoint.y + 0.1f);
	 * 
	 * // if we hit something we create a new mouse joint // and attach it to
	 * the hit body. if (hitBody != null) { MouseJointDef def = new
	 * MouseJointDef(); def.bodyA = groundBody; def.bodyB = hitBody;
	 * def.collideConnected = true; def.target.set(testPoint.x, testPoint.y);
	 * def.maxForce = 1000.0f * hitBody.getMass();
	 * 
	 * mouseJoint = (MouseJoint)world.createJoint(def); hitBody.setAwake(true);
	 * } else { for (Body box : boxes) world.destroyBody(box); boxes.clear();
	 * createBoxes(); }
	 * 
	 * return false; }
	 * 
	 * another temporary vector Vector2 target = new Vector2();
	 * 
	 * @Override public boolean touchDragged (int x, int y, int pointer) { // if
	 * a mouse joint exists we simply update // the target of the joint based on
	 * the new // mouse coordinates if (mouseJoint != null) {
	 * camera.unproject(testPoint.set(x, y, 0));
	 * mouseJoint.setTarget(target.set(testPoint.x, testPoint.y)); } return
	 * false; }
	 * 
	 * @Override public boolean touchUp (int x, int y, int pointer, int button)
	 * { // if a mouse joint exists we simply destroy it if (mouseJoint != null)
	 * { world.destroyJoint(mouseJoint); mouseJoint = null; } return false; }
	 */

}
