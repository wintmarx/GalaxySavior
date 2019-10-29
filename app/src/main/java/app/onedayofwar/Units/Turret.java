package app.onedayofwar.Units;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import app.onedayofwar.Field;
import app.onedayofwar.R;
import app.onedayofwar.System.Vector2;

/**
 * Турель
 * Размер 5
 *
 *           Х
 * Форма    ХХХ
 *           Х
 */
public class Turret extends Unit {

    public Turret(Resources resources, Vector2 position, int zoneID, boolean isVisible)
    {
        super(isVisible);

        if(isVisible)
        {
            image = BitmapFactory.decodeResource(resources, R.drawable.unit_turret);
            stroke = BitmapFactory.decodeResource(resources, R.drawable.unit_turret_stroke);
            icon = BitmapFactory.decodeResource(resources, R.drawable.unit_turret_icon);
            iconPos = new Vector2(position.x, position.y);
        }
        this.zoneID = (byte)zoneID;
        Initialize();
    }

    //region Initialization
    private void Initialize()
    {
        if(isVisible)
        {
            //для прямоугольника -76
            ResetOffset();
            pos = new Vector2(0, -image.getHeight());
        }

        form = new Vector2[5];
        InitializeFormArray();

        accuracy = 100;
        power = 1000;
        hitPoints = 1500;
        armor = 1000;
        reloadTime = 4;
    }
    //endregion

    //region Set Form
    @Override
    public boolean SetForm(Vector2 startSocket, Field field, boolean isInstallUnit)
    {
        Vector2 tmp = new Vector2();
        Vector2 startPos = new Vector2(startSocket);
        Vector2[] tmpForm = new Vector2[form.length];
        Vector2 sizes = new Vector2(field.GetSocketsSizes());

        for(int i = 0 ; i < form.length; i++)
            tmpForm[i] = new Vector2();

        Vector2 tmpLocal;

        for(int i = 0; i < 3; i++)
        {
            if(field.IsIso())
            {
                tmp.SetValue(startPos.x - sizes.x * i/2 , startPos.y + sizes.y * i/2);

                if(0.5 * (tmp.x - field.width/2 - field.x) + field.height + field.y - 3 < tmp.y)
                    return false;
            }
            else
            {
                tmp.SetValue(startPos.x, startPos.y + sizes.y * i);

                if (tmp.y >= field.y + field.height)
                    return false;
            }
            tmpLocal = field.GetLocalSocketCoord(tmp);

            if(field.GetFieldInfo()[(int)tmpLocal.y][(int)tmpLocal.x] != -1)
                return false;

            tmpForm[i].SetValue(tmp);
        }

        startPos.SetValue(startPos.x - sizes.x, startPos.y);

        for(int i = 3; i < form.length; i++)
        {
            if(field.IsIso())
            {
                tmp.SetValue(startPos.x + sizes.x * (i - 3), startPos.y + sizes.y * (i - 3));

                if (-0.5 * (tmp.x - field.width / 2 - field.x) + field.height + field.y - 3 < tmp.y || -0.5 * (tmp.x - field.width / 2 - field.x) + field.y - 3 > tmp.y)
                    return false;
            }
            else
            {
                tmp.SetValue(startPos.x + sizes.x * 2 * (i - 3), startPos.y + sizes.y);

                if (tmp.x < field.x || tmp.x >= field.x + field.width)
                    return false;
            }
            tmpLocal = field.GetLocalSocketCoord(tmp);

            if(field.GetFieldInfo()[(int)tmpLocal.y][(int)tmpLocal.x] != -1)
                return false;

            tmpForm[i].SetValue(tmp);
        }

        if(isInstallUnit)
        {
            for (int i = 0; i < form.length; i++)
                form[i].SetValue(tmpForm[i]);
        }

        return true;
    }
    //endregion

    @Override
    public byte GetZone()
    {
        return zoneID;
    }

    @Override
    protected void ResetOffset()
    {
        // test (-76,0)
        offset.SetValue (-86, -7);
        strokeOffset.SetValue(-4, -4);
    }

    @Override
    protected void ChangeOffset()
    {
        if(isRight)
            offset.SetValue(-86,-7);
        else
            ResetOffset();
    }

    @Override
    public void Update()
    {

    }
}
