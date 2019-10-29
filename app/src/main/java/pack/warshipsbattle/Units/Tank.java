package pack.warshipsbattle.Units;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import pack.warshipsbattle.Field;
import pack.warshipsbattle.R;
import pack.warshipsbattle.System.Vector2;

/**
 * Танк
 * Размер 3х2
 */
public class Tank extends Unit{

    public Tank(Resources resources, Vector2 position)
    {
        super();

        image = BitmapFactory.decodeResource(resources, R.drawable.unit_tank);
        stroke = BitmapFactory.decodeResource(resources, R.drawable.unit_tank_stroke);

        startPos = new Rect((int)position.x, (int)position.y, (int)position.x + image.getWidth(), (int) position.y + image.getHeight());

        Initialize();
    }

    //region Initialization
    private void Initialize()
    {
        //для прямоугольника -78
        ResetOffset();
        pos = new Vector2(startPos.left - offset.x, startPos.top - offset.y);

        form = new Vector2[6];
        InitializeFormArray();

        accuracy = 0.5f;
        power = 1.5f;
        hitPoints = 3000;
        armor = 1000;
        reloadTime = 3;
    }
    //endregion

    @Override
    public boolean SetForm(Vector2 startSocket, Field field, boolean isInstallUnit)
    {
        Vector2 tmp = new Vector2();
        Vector2[] tmpForm = new Vector2[form.length];
        Vector2 sizes = new Vector2(field.GetSocketsSizes());
        for(int i = 0 ; i < form.length; i++)
            tmpForm[i] = new Vector2();
        Vector2 tmpLocal;

        byte num = 0;
        byte toJ = 2;
        byte toI = 3;

        if(isRight)
        {
            toJ = 3;
            toI = 2;
        }

        for(int j = 0; j < toJ; j++)
        {
            for (int i = 0; i < toI; i++)
            {
                tmp.SetValue(startSocket.x - sizes.x / 2 * (i - j), startSocket.y + sizes.y / 2 * (i + j));

                if (-Math.abs(0.5 * (tmp.x - field.width / 2 - field.x)) + field.height + field.y - 3 < tmp.y)
                    return false;

                tmpLocal = field.GetLocalSocketCoord(tmp);

                if (field.GetFieldInfo()[(int) tmpLocal.y][(int) tmpLocal.x] != -1)
                    return false;

                tmpForm[num].SetValue(tmp);

                num++;
            }
        }

        if(isInstallUnit)
        {
            for (int i = 0; i < form.length; i++)
                form[i].SetValue(tmpForm[i]);
        }

        return true;
    }

    @Override
    public byte GetZone()
    {
        return 3;
    }

    @Override
    protected void ResetOffset()
    {
        //для прямоугольника -78
        offset.SetValue(-69, 0);
        strokeOffset.SetValue(-5, -4);
    }

    @Override
    protected void ChangeOffset()
    {
        if(isRight)
            //для прямоугольника -52
            offset.SetValue(-42, 0);
        else
            //для прямоугольника -78
            ResetOffset();
    }

    @Override
    public void Update()
    {

    }
}
