package me.pig.pack.impl.ui.menu.taskbar;

import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TaskBar extends Widget {
    private final List<String> motds = Arrays.asList(
            "discord.gg/JyWDJSxh", "wurst+3",
            "pull my devil trigger", "skoschukha",
            "ratted by cattyn", "back2school", "go eat some bugs",
            "W202 learnt about the Internet yesterday",
            "welcome to the los pollos hermanos"
    );
    private final SimpleDateFormat format = new SimpleDateFormat("MMM d hh:mm:ss aaa");

    public TaskBar(double y, double h) {
        super(0, y, 0, h);
        Collections.shuffle(motds);
    }

    @Override public void render(int mouseX, int mouseY) {
        RenderUtil.drawRect(getX() - 1, getY() - 1, getPointW() + 1, getPointH() + 1,0xFF433856);
        RenderUtil.drawRect(getX(), getY(), getPointW(), getPointH(),0xFF291F38);
        String date = format.format(new Date(System.currentTimeMillis()));
        FontUtil.drawString(date, (float) (getPointW() - FontUtil.getStringWidth(date) - 2), (float) getY() + 3, -1);
        FontUtil.drawString("pighax, cattyn", 2f, (float) (getY() + 3), -1);
        FontUtil.drawCenteredString(motds.get(0), (float) (getPointW() / 2f), (float) (getY() + 3), -1);
    }

}
