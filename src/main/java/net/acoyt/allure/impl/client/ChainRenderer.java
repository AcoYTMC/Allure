package net.acoyt.allure.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

/**
 * @author AcoYT
 * Code from <a href="https://github.com/eeverestt/hibiscus/blob/master/src/main/java/com/everest/hibiscus/api/modules/rendering/world/ChainRenderer.java">eeverest/hibiscus</a> with permission
 */
public record ChainRenderer(Vec3 start, Vec3 end, double sin) implements SubmitNodeCollector.CustomGeometryRenderer {
    public void render(PoseStack.Pose pose, VertexConsumer buffer) {
        start.add(0, 1, 0);
        Vec3 direction = start.subtract(end).normalize();
        double horizontalMagnitude = direction.horizontalDistance();
        double angle = Math.acos(direction.x / direction.horizontalDistance());
        if (direction.z > 0.0) angle = (Math.PI * 2 - angle);
        double angle1 = Math.atan(direction.y / horizontalMagnitude);
        int segments = (int) Math.ceil(end.distanceTo(start) * 2);

        double backOffset = -0.15;
        double upOffset = 0.05;
        Vec3 startOffset = new Vec3(
                direction.x * backOffset,
                direction.y * backOffset + upOffset,
                direction.z * backOffset
        );

        pose.translate((float) startOffset.x, (float) startOffset.y, (float) startOffset.z);
        pose.rotate(Axis.YP.rotation((float) angle));
        pose.rotate(Axis.ZP.rotation((float) angle1));

        for (int i = 0; i < segments; i++) {
            Vec3 vec = new Vec3(0.48, 0, 0);

            if (i % 2 == 0) {
                pose.rotate(Axis.XP.rotation((float) (90 * Math.PI / 180)));
            } else {
                pose.rotate(Axis.XP.rotation((float) -(90 * Math.PI / 180)));
            }

            plane(pose, buffer, vec.scale(i), 0.6F, 0.6F, 8, 8, 8, 8, 1, 170, LightCoordsUtil.lightCoordsWithEmission(15, 15));
        }
    }

    public void plane(PoseStack.Pose pose, VertexConsumer buffer, Vec3 offset, float width, float height, int frameHeight, int frameWidth, int textureHeight, int textureWidth, int frame, int alpha, int light) {
        Matrix4f positionMatrix = pose.pose();
        if (frame < 1) frame = 1;

        float xOffset = width / 2;
        float zOffset = height / 2;
        float uOffset = (float) frameWidth / textureWidth;
        float vOffset = (float) frameHeight / textureHeight;

        int color = 0x58c2c5;
        int r = ARGB.red(color);
        int g = ARGB.green(color);
        int b = ARGB.blue(color);

        // Vertices in clockwise order, starting at top left
        buffer.addVertex(positionMatrix,  (float) (-xOffset + offset.x), (float) offset.y, (float) (-zOffset + offset.z))
                .setColor(r, g, b, alpha)
                .setUv(0, vOffset * (frame - 1))
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);

        buffer.addVertex(positionMatrix,(float) (xOffset + offset.x), (float) offset.y, (float) (-zOffset + offset.z))
                .setColor(r, g, b, alpha)
                .setUv(uOffset, vOffset * (frame - 1))
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);

        buffer.addVertex(positionMatrix,(float) (xOffset + offset.x), (float) offset.y,(float) (zOffset + offset.z))
                .setColor(r, g, b, alpha)
                .setUv(uOffset, vOffset * frame)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);

        buffer.addVertex(positionMatrix, (float) (-xOffset + offset.x),  (float) offset.y, (float) (zOffset + offset.z))
                .setColor(r, g, b, alpha)
                .setUv(0, vOffset * frame)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);
    }
}
