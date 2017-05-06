<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<div id="orderDetail">
    <div style="height: 130px; margin-left:500px;"><img src="${base}${order.barCodeImgPath?if_exists}" /></div>

    <div id="page1" style="width:90%;margin:auto;padding-bottom: 80px;">
        <table cellpadding="1" style="width:100%;margin-left:37px;">
            <tbody>
            <tr height="20px">
                <td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">收 货 人:</td>
                <td style="text-align:left;font-size:80%;font-weight: normal;">${order.consignee?if_exists}</td>
                <td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">订购日期:</td>
                <td style="text-align:left;font-size:80%;font-weight: normal;">${order.orderTime?string("yyyy-MM-dd")}</td>
            </tr>
            <tr height="20px">
                <td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">付款方式:</td>
                <td style="text-align:left;font-size:80%;font-weight: normal;"><!-- <#if order.paymentType==1> -->在线支付 <!-- </#if> --> <!-- <#if order.paymentType==2> --> 货到付款 <!-- </#if> --></td>
                <td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">拣货编号:</td>
                <td style="text-align:left;font-size:80%;font-weight: normal;">${order.batchCode?if_exists}</td>
            </tr>
            <tr height="20px">
                <td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">收货地址:</td>
                <td style="text-align:left;font-size:80%;font-weight: normal;">${order.fullAddress?if_exists}</td>
            </tr>
            </tbody>
        </table>

        <table border="1" style="border-collapse:collapse; border-color:#000; width:100%;margin-left:37px;">
            <tbody>
            <tr>
                <td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">商品编号</td>
                <td style="text-align:center; width:40%;font-size:105%;font-weight: bold;">商品名称</td>
                <td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">单价（元）</td>
                <td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">数量</td>
                <td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">金额（元）</td>
            </tr>
                <!-- <#list goodsList as item> -->
            <tr>
                <td style="text-align:center; width:15%;font-weight: normal;">${item.skuCode?if_exists}</td>
                <td style="text-align:center; width:40%;font-weight: normal;">${item.skuName?if_exists}</td>
                <td style="text-align:center; width:15%;font-weight: normal;">${item.unitPrice?string.currency}</td>
                <td style="text-align:center; width:15%;font-weight: normal;">${item.quantity?if_exists}</td>
                <td style="text-align:center; width:15%;font-weight: normal;">${item.subtotalPrice?string.currency}</td>
            </tr>
                <!-- </#list> -->
            </tbody>
        </table>

        <table border="0" style="width:100%;margin-left:37px;">
            <tbody>
            <tr>
                <td style="text-align:left;font-size:80%;font-weight: bold;">序号:${rownum?if_exists}</td>
                <td style="text-align:right;font-size:80%;font-weight: normal;">合计:${order.orderAmount?if_exists}元</td>
            </tr>
            </tbody>
        </table>

    </div>
</div>
<div style="position: absolute;left: 0;right: 0;bottom: 0px;">
    <div style="text-align: center;font-size: 60%;">
        <img style="border: 1px solid #ccc;padding: 4px;margin-bottom: 3px;width: 65px;" src="data:image/gif;base64,R0lGODlhtgC2AOYAAAAAAMjIyFtbW+peACMjI62trfveyJmZmfGQP/Pz8+3t7XNzc/i8ihEPEO53HffBlDo6OvWze5SUlP759fSuc4uLi9TU1MXFxTc3N2dnZ1JSUu+ILPOiW+1tCWtrawwMDPnRrry8vOxsAO+BJHt7e6SkpPKZTNvb2/3x5UNDQywsLO+EK/a5hPjJn7Gxse12EvCIL/OoZvKWSffGnPGOO4SEhO5+I/rbv/CFKxwcHPKdVO55FeDg4PGTQ+tlAPB8HPjCle5wBvCBJffInvOlYvW2gUlJSfa9jO1zDP306/WxdfrSsv////nOqvCLNPvl0vCGMPKgV/Sra/jGm++OP/B/IffLpPzjzRISEu58G/B1DvCOQ++FMfB5Fe+KNO9/LffFoPnKpPnWuPi/kvrcxFB3xQAY9wAAARHkAAB2LwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACwAAAAAtgC2AAAH/oBMgoOEhYaHiImKi4yNjo+QkZKTlJWWl5iZmpucnZ6foKGio6SlpqeoqaqMGRiur7CxsrOzFYQWsCkJjgW0GLqJFb7DxLaDuL4WhMIQxM7DGYoqANTV1tfY2dkChBfXCo4S2tTgiALj6Oncg97jF4Tn6fLpKooY8/ja0YMB1ljljA6M+5coQ758+wT1GxeAkMGDEAFgsBcRYkImC6kRbCRQ28ZDDyuOu5gxW8NBIUXKm5jonsp5JP0BXNQx20dD8V5iW3AL3TuUOldSrCbggNGjSJMqRQrBWsxqNxXVxBa1G9KcAAgsPUpgnIoMYDMs2Gp0ZkoIZNMqxcoSkUtq/iUyeXBKqGRVRFOv3T1UwhoERU3n1XOU0kOmvtXaHnoL4ECmlE81zpQ6cHKiqYoNMUaXWRHkTJiHUnOM6TM/mY7youZorTOhzeNcIzJ9KXRLa6Qv0cbo75Fqa5YRiatGQHS6v4TpYrLtFvetrRIkJDWrXKG1DxWMSp95AWmNsBk8gF+wq9EF8Maaz9OK1HN1JgrUIlU2iPliaxIIDZ/3U9Duktj0xwRWF5UCGz7B+fdeO/PkV19rxuXGxG/anKRgNZGZ5NB7Bla0FxP/5SOhfZo59yA+FoL4HoDXpLhbhxF9GCI+I0J4WzU1orghht2gkyKBqUwTo3s8nkYjISQW/sJYjvO4+B4yr7S4DCwLBGDllVhmmWWCTCSgJZYaPIOBkNR8EAKW5V1ITYbpMClRhPrl42SRhySAhTUpFkKhPA4ewqCGjbBojYAzNoikjeqNdqiOQNFpiAJ3VpPnohH1aYigkjqCKTVzrlmXiJS+eaOiJza5o6eIQIrnZSJJWMimAEyKCKydAsAmOm7KJsiSoaZTa4GEKPDBoInsB5Grn7rjyJ/Y1GqYkfPkCmep8vyaiAIEYKEtFrIKcsC24G57zQfgSqDAueiiG0K44XZ7abhSNoqqIMziGqquTPBKra+n2qpIAukuku7AJ0RKTQjpesDutikMPDAk6Ra8qrz+/kIrj7SjNtarj/0C24mdE6uJDb6UgJwpxbeOg3GiGu/LMcqjqHqyyNeQPInMnHacbLT3TuutnDqLImzIKmpjsyQ4xxq0dfhYmuRrJgpibDqEcujJ0DMXnQ1ynCRtLTv5OI3ofTgG++XZaWpdsSBQ/pK2Ir00g0GBWYbpCgQVYLnAdb5AQN8hvbxyUdJVO+rl2VdeYOVMTw+ir24LfhOONYMhQmbLgiDGX7GUB2uw0jAvN3aJZZcWOVRc6jm6klFPCDReY3u9tCWN79p6JYUC8KHqifnsOqOH2Cd76LWtDnU1cWEyl6N2pR5n74lc7mY6fxsi/OfOHma8436B5/33/uCHdXmGN1UAnoBTcd3lAuGBld1RJIBHAlJ7V4Md/ef3PMjwIn8V/v/gCww18HWgoKxoNYLoSjXEBj1BKOAa1dvNCXpDiMsxcICeI1pKDDgy43DwGuSbCWMuKCoHYm92f7rJ436Hwf2dkGIf3J7tYoiNEB6PVD9rIHxeKDIbcq90OWyhCTVIww4mQoFFJMrOdDcTJGKOhQConCAgOLsJou6GAEhe5mJ3DQFhJYnFCQYAx/g/8YRFi7y54iDMF5YaeGc8SJGAGd2HlPiFZX5HqZ9kCCFAABTlKGwhRALYF5YTEKIEYJnjHMnYPgCmZxWvQqAhdmOBaxRCehtT4wyF/gLJTgZKkoXInQpbt6dRzuNonoRk8wpyQE3mi5SVwSJnUknLWVFwNtZYB9M0Uog+onFPABAhPtRXy5shTktpOwGWDGkxMyFuAbB4ZEmceaXLVelKwqAFMH5IDQLAwokqOKY4sVQ9Q1ggS4r7UjkfMTWfzA5W10CWxQ7iMZZNz4AExAcqDdFOhrwTVLasSD3JhkMocjCfp6QEMLv4zyMFNCIDJV1BF1oRhHJSEhTN2m7qpbJEwHMcz2rECJ8XQ4umY58AQ1cF0BEuhKFLYdviCT/gNY4KOExdNIVJut5WiBSAa0TgGpY/wJUOcKWgEAPz6cKwIFQhMmIBQR3ICdKl/lRtmQtdb4vY58a1VCw0DF0Sw4dR/xUwpKrLH1NFVwFttdNgZWtbLr3pSnW4iA0OJG0r/NdW9SnIvSYUE3ZJWx+vEdJHNRV0wZPhJMUqTCAKzK8X3WFFM7FKbtbwWjy0nmJDyVgsyvMQScuHYkLLV8D6A68gxSzRCmGsfdrVI40tqF4nOwiTQWSfipimYFObqszyzqmTjKbeYmlZbxJDbvnQAJYCFyXcgMkayr3SOnkBi+gG4AIhSAEG5HZYvcBicJnNpiuued3pDgKTg9AcVWKbRAAUtpLjqN7yqFHYShhLikwQiRT5NyCrZeyerlwrB1OmUf9i1C8ZjMhoM/si/t+Vkr1JJDA1Cjcv2sUOsidNcM6Ix4gVAhPCRbyIFbXBTEHM172ZuG8hRBLGIVajxGqLKOscOxX2GAWNrySKfI4inR1/JywaQIcGwHM5/zHSewKyAFLYmMgeG8WJ10CLdgqgYQAMuY1vDMsjJfrE9BkHx6Bpb9MSAd9q9JK3oMWwm1p8iD6ujKBP1EQ/xYyNz6Zxj5YFoWq18WYu93nGsg1zNVIggEIb+tCITrSiF52C2y3RlNkYKGlprFgnkpCYgM6iJwQiABe4wBQuGA6Yj2FJPo5Dl4ewV3oVy+QMoM8aNjZKpv+4Y/lIRwIkqK8pSmAEDZCFjeLxgJMP4EQI/oAHjfHh8SK950ZAbpZV4wAxDWW6ijJnA7/oGDW9So1LoymUuJtMIqpXUYLu6nDSgbaYjGLzbdjKkoYhUEQSUEDvetv73vi+9wQUkQNvu7CjD2UiK/0tCfXq5ZJ0/rQhJjAEHeBgBBCPuMQnHnEbCOHiI7DBCITQAyU8oU6QxTbA/cRtkLDbEcq8kh6x4cx0hkluYnIFOozrAZ5egQoi8IEIds7znufc50DX+c594IMXHIEvtLBuALLri7xdCcZtS8GZzgbNVzjRtSJx12z5nIgr/EDoQA+72MVO9Ag8QrcDr3AhEmBuStOVSBHR+p6z8dkJ0ADsY8+73nUOAk2d/jYRJ5a0mvXXiNeaCmmDP8QM8O7zHfxA70FnvA9o4HdXctZRSE18EEvICpEI6BG2rTMidMB4nvugB1ZAQtiR4IQVuH4FUHC9E2gAdCQY4JP249KJdf1vrlML60PtKrhSlNKbEgzDNlVA2pKwgtLn/PQRIDrQX0ADJ1jfC07AAQLIsATV+xwM18LptgjAJci0tffZSP65JDDW5GDI+DctBFSFv6222w8LDbgICmwAdBNwwAlcIAUP4AXYdwS013NCR3RZEAEc4H0I+AB4YVTn121QwTBVxlXb4gET2HnVwHuFF0MaQAgoMAI+5wMRUARfsAIx8ACyJwYy4HxZ0ANE/tB8YecDEHgIrfUIhsd56HZZk9Bg7sdBqDaCJUgBOjAAk6cE0scCOFCDHPAAPxeFpneDmvV2aWdELiYPMnaFW4gIJxYUQ0iCCEgBDyADMhABYWACJqADYdCEIvACWbADWfADLyAFRACHO4ADESCGQ0eFv8V5dXVyDmR/PigJQPiBHxSGRUgEHdABJlAESIAERueGONADNHCJTlAEETB7TvCIB9iHsGOFFIgN6sN2+NCF/iFc2OQL25SKr+B0X5JR1xCGeOcDSnCEPoAAEfBzR7ACNagDpDd0kueHF2B1itUKrzhc9pMLU2cl1rRMj4Bem4cNMyGN5hQRioiAEXAE/rNHAVOAAAggA02AA6XnAxzAAXlng4RQAAR3CNYITGeGPJTgYeCWY+m2S/mQjaZnhw6QBRxwBFmQBSOgA3IYdOeYjn7oAu3oZ5mEZ+FmZ4tAj+5mWXb2Udigjzz3AkPXAxRAdEQXBCYAdAOgA+g4duo4CAqZDSYFYA5pj3H2CIyBRgZXckzgS4nAUeoggnwYdD0ABDvwAkApA1bgczhAA0dQkmTnh+yokoCBHw0pcINgafPYPWHRR7EmR2GhMFQpHsgWR2kRGBgJdFlwBCxwgkUwA1LgcysQAVcwAs4HioPAAz0WSLVFSGDxOSwJlYJQAPMxlfNATPlICcszhEIA/nk/KYcOaHoikJg16IeHxEUjZ1CglAkCdmGnSAkPQYtjRwM6sJhSiAR0CJSRCJRA6QSdiYBAEIpO1YN5uTt+GVk9qGc/SA1hmXMRIAMDwAIzIAJOQAFOwHMjEAU9MJwtsIZjoAM9oANEkJgnWYWrqXmSaXmUiQ9s9kD4MG6PMJg6WYI2AAJN+I1Eh3pIIHRBgABAaQVZgANHYANZIANB55iDoGKEAJ0f5gmMYWzgMUePNEhHpm3eQhZgqZOMFwFSMAA/MAM7wHNZgAC/KQI7MAPIuQQmEAVpqAM38HgPiAjnERZjcRQVcFgaMEfNZhQ/Bj7k4QhKdhRUljH++QnW/pmTg4AChdlzIwACHLAFStAEPWCGw1kESrBzO8ACGjkDSJAFDCCJUFiC8HkI1oYNMHaIkWAsbMaQpxCbtLmdPqcFQSACEdCZQbCljQcEMuAEEYAAHBmODwCmU4h7FTI7lVA7LgmRV4NhShSjO+lzTcABOJB9Rwl2SMCgMCAEMAADOIADRHCaGcoItOKm7QZcCLdAqGClViagQScDD0AEOGADUGAASJlzMXh92OcFRIAAw9gITUpFFIOdkzAV+BVKsLCidZIlhXBOZwNjmUc5s0ACIjijQHcEOCADNDCSRwB2O7ACIxCHAZmscigFbgiXj7JcsRBPenM3GLCfsnoL/ogjXnOTCX/yAdKmdlUmd0wwgpJnAtG3gDgwBW4JnGPaAzFAA1IQATSAADBAA0QABYnKT2MTevF0k9fxrWuzCZUVbrKZZqt1CEToc1nAAlmQczqQBFEwACXYAUcQfTKwnJYoAhKbr6xlmb53CAPrkgUrsJK0VoJ3sIaQsBnJrD7QAT3AAkVwBDqgkR2wczbAAujoAzIQAz6wgGHgBUoqHB5Ldx5VsiPhCYH1bpgnSIf1eQi7rjuHBCaAA+1JAUpQBT6wAkewBA9AeyOgpz7wp2GQljlHASwQtImlQ/zqlAGHBbulDR5YJ2UVLGcFFWl1LlXFLtTmQGD1VtpCfOeS/jYqu5gv0AU7+nU9hwMmoHoYunMbwADN+oZAl5p0qwDsxzBadR0/5a92my4mu4GGkLdAVX96AS5x5TBpEwDgQgA8MLeCMH8XMQE9wHhb6nxvCXkisASE4AIZmC7rQrrWcLq5VbriwlIWmDGWMmeAwgjdylONEoKEoIS4O71jlwUosI7M8zKKik8O9jrbK52NMm5PoJHUW76mZ3a7m73+xKYvsZJP2SyVpxHOeyHYOazmW74+4ARJUAgpGbCLyr4q4b4us77Mex3zWzSquou3e7/C6AQfVwhL6b/auwgWGVlUunnhdCUhcFgpoq01IEhYwnSvEEHUYATOCwI0oHoe/rnCLNzCLqxzWaAE+/usVqKtl4MNKvCq6wgLNyyt4xQA5iUIbhYqxPQ5XpRLisBQ8hJviEAGQ/AAUBzFUvwAQAAEUxzFVhzFS3C9ASESyZsPQQwJEvmcRLS06IdYWmMptCSLojfAbYoJY8yDDGZgklVg1QCrqnACTDyNAMXHBGwJMRkq+PU5JGzGWQgAMHZiKSCunxAAGaCr8dkq73sNtvqaAICfYEECTmY+ZvRFVwYWI3oAJZoBj5Q0n5wBPVwCCXDAn3ABElA9FuBkK3c/W2FHmfxrIJo/7IAeHvQkfQxF1Umn2ZADOSABnnbMyOwCM2EByBzGyew8gzBiugN3/gFrCBj2xcTRy+rLM793gQZ0xHUKWkr8vS15eeB6xv3azRkzshUMACS0YEX0NQabNcMLvj00d228eSY1stLcJo95bsJcEYV8pYmAqgUMHImAFap6yOm8eVOaaZj8yDy2cthwyqFMl4d8yuGzbN4jHu9jFLacAV2pbOhBFgJyAuNxwIh0Rvhs0VnmPl82ydbwpIJYx9RQyZNgjYXALI+6DTHzOTRtyHCWl/ALQ0OLxpWQV48GsHGrCfxVGDAJS/hAYVho00htyWq81Ep7zl0zx0J9wdH5xoLwRZ1zxmEcjWwLskYLt6RAyISwe1HtdmUSrUSjrbEAXs51JTOhAMfk/qrbhQF4HEkBNg5Kd9bXkk5assF5bSVBVg2F3b3VUIq+5Qg9iM3Z4K2TELKVKYqMoLwBDNlkTM+Ufc0ktV6ZvdYWzE5FJMD6HK43Q5/1eHaonWGrutq+k4P707STYIraQELjwspFO9h/2agfxNoT0n4O5LfcIkjwRzDKHS7Ts7p36zCPUrctyRjkArwD1NzNPVceQX9LdVT/ZVaBS97nIn/grS2sC39/iAERkwNdNbracliQBgDqpwDerRHpzS6EiAXTzd2gm2lyGoTp8AHATS2lSIiWbdoUKdM64bb26WizOQ+uqc4MndZhrZcu6dtFVOGSMFKmQ+EH3tro3DrK/gvhDe7GH4TinADikCPiqYFgtQWdJ45act1eLL4JtLo4mZBysVgzsFA92upEH0DXHwsfWJIXsVAA12UlVTdeSY4rPxwASh7kicBc21pMp0osg0DWFWFnW14NAx2wYU4Nhs027iS01dCqnfS/NCPJAVfU9zxP8ULOy+ucgOhJbq42KlGRE5w7ck7BE/yHmNZJOLlhY21AYJ7mc46PgV7PJMY52SxGR1bp3jPSRvGhRNMdW0HRqkYvcEQW1LHNd77SYIFGsHLKSYYUgV0IUFZECy0Ik40I/fzpTCCfiFhhe67QS5QOWe0Im60TJyvacW7rcGrOEvzHgG7rkDBYIYbP/ox8ZxdDeByo63/eSoYyCcH+EsOO6II+ZpG85tlpDYVV60TD63Q+Di1qHBFt6VmJynTMX5xuFAKS6o0kIBsKFh0KHUzhF/QzwSvNlb3uPZ9DawfgtCwqF/GeWeju6JSMonTm7ZA+zaamDbEO1riz8GVs7X888e0V7Zp9tMAu4Yao8XbM8WL97XSG8Got3JEW1/cYCVDKBG49CIHX63W+CGX+QWfOBNZ2Ez3cgTD/RHw95WmzG4djJYqNI89F6o9uNleC5bLQ38Pgw+M0FTeBjK7wOY893uG+ORx26OlQWHvesZxdk2hmCGsbI7pX02DNxvIs7QdB9oNe2vtM7vgs/hIf8oWKFcf8wmHtzM51b+FwNrK3+uDl5/YC7uDevuz4QPcdT+1cRljXQoj5kOPIXjOgXS0NBVG+m1MHA3/5nRlJRVROsYHYst/hQt/Tjd7aneeexfho7Phihblg9TnZvTCHNVrKfbobhNx8C+AOEwLE+7fMrVJ9L9WH1+hyLxKKsfawycFvrfinrSyqCfsp7scpT/u35c1/dci1ovmmFfmE//beG2M4T1sX/v1WHf7PZh6DLzXJf+N/z/yBX9OSujV9tfFVXQliz/KAwCQBQAiAwYSYqIhRCHCgeNAoORmgmNGYoRgw2XgQ8Bmg0agBGmChqIDVWQp6EbDQ+IEx/jsrWZl4wlpaQIuRkmDZK1yhmBDgqhsArIhaesq82AnJSX2LeFmYmbhJDfCM6NHoAZ2Y2vgNXdJNSU5uIamgqNIt0G5/j5/IWPiYGLneyBoTbIS0IeJWzRIme+YKCWT2DyChh/YQAsAST183g/k6dtxHqB+iiBIFEgTA0SK7RCfHtWtI6II9kgAptrOIMRo1jh57tgPpSNEgiTEVZlN0YZ1JcQxVFToxk+hEj0kbZUQEVJJLn1yhARXJxMKBsWTLmr06cGG5shU+BDRKSEWGuRmIlXNKSAPdDDL9SQUgsMTeEqjMMsvaSO5cD3YRJViwN3Lfrl+7kjvJkxlewHAn/hFAtVmShGlSBQpoVM8jYmqfy60bbbmyZWaYm75l2e3Q3W5gaSrtnNneak66EcGcBNYnUNizcR99uXkpteJMjkvq/Vd6wZ7DJ1G33og5V6CEmycK95ycAkmT005HtQ7sUKLaAWzNN49oa+PxZwMVYFaAApIlwQEFQsBUO4/tBZVznEBQDGR0bQbWBZHtddpt1zQCwYACSjhXfnHt1RgTC144Vw1ltYdPd38BFFxHJ0lCHTQI8tNTdJ29iA56hNzn00mpffRikdtZNmMjNR4mTUfH1fdifTHmU5tqRhY5JT5JFrKkTiF59OSO2QE3W5UdiXglUUNylSEnKtyDpnj3WKzXSHttFmknarPduaaWwvwJaKCCluhTBYJmmUEvBXiUQAq9oFODoL1A8Cc6htJCaE+XzpKpeZ5+Cmqooo5Kaqmmnopqqqquymqrrr4Ka6yyzkprrbZyFQgAOw=="/><br>扫一扫查<span style="color: red;">&nbsp;电子发票</span>
    </div>
</div>
</body>
</html>
