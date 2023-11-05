<!DOCTYPE html>
<html>

<head>
    <title>${title}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <style type="text/css">
        body {
            font-family: 'Lato', Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0 !important;
            padding: 0 !important;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }

        .header {
            background-color: #FFA73B;
            padding: 40px 0;
            text-align: center;
        }

        .header h1 {
            font-size: 48px;
            font-weight: 400;
            margin: 0;
        }

        .content {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 4px;
            color: #111111;
            font-size: 18px;
            font-weight: 400;
            line-height: 25px;
            margin-top: 20px;
        }

        .content p {
            margin: 0;
        }

        .button-container {
            text-align: center;
            margin-top: 30px;
        }

        .button {
            font-size: 20px;
            color: #ffffff;
            text-decoration: none;
            padding: 15px 25px;
            border-radius: 2px;
            display: inline-block;
            background-color: #FFA73B;
        }

        .button:hover {
            background-color: #FF7C00;
        }
    </style>
</head>

<body>
<div class="header">
    <div class="container">
        <h1>${name}</h1>
    </div>
</div>

<div class="container">
    <div class="content">
        <h3>${title}</h3>
        <p>Kayıt yaptırmanızdan dolayı heyecanlıyız. Önce hesabınızı onaylamanız gerekir. Aşağıdaki butona basmanız yeterlidir.</p>
    </div>

    <div class="button-container">
        <a href="${activationLink}" target="_blank" class="button">HESABI AKTIF ET</a>
    </div>

    <div class="content">
        <p>Herhangi bir sorunla karşılaşmanız durumunda, <a href="#" target="_blank" style="color: #FFA73B;">${activationLink}</a> adresini tarayıcınıza yapıştırarak işlemi tamamlayabilirsiniz.</p>
    </div>
</div>
</body>

</html>