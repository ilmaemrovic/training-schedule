package raf.microservice.components.notificationservice.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import raf.microservice.components.notificationservice.service.EmailService;


@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMessage(String emailReceiver, String name, String messageToSend) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("mkarisik2002@gmail.com");
            mimeMessageHelper.setTo(emailReceiver);
            mimeMessageHelper.setSubject(name);

            String htmlContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html dir=\"ltr\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" lang=\"en\" style=\"font-family:arial, 'helvetica neue', helvetica, sans-serif\">\n" +
                    " <head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
                    "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                    "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "  <meta content=\"telephone=no\" name=\"format-detection\">\n" +
                    "  <title>New email template 2024-01-02</title><!--[if (mso 16)]>\n" +
                    "    <style type=\"text/css\">\n" +
                    "    a {text-decoration: none;}\n" +
                    "    </style>\n" +
                    "    <![endif]--><!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--><!--[if gte mso 9]>\n" +
                    "<xml>\n" +
                    "    <o:OfficeDocumentSettings>\n" +
                    "    <o:AllowPNG></o:AllowPNG>\n" +
                    "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                    "    </o:OfficeDocumentSettings>\n" +
                    "</xml>\n" +
                    "<![endif]--><!--[if !mso]><!-- -->\n" +
                    "  <link href=\"https://fonts.googleapis.com/css2?family=Imprima&display=swap\" rel=\"stylesheet\"><!--<![endif]-->\n" +
                    "  <style type=\"text/css\">\n" +
                    "#outlook a {\n" +
                    "\tpadding:0;\n" +
                    "}\n" +
                    ".es-button {\n" +
                    "\tmso-style-priority:100!important;\n" +
                    "\ttext-decoration:none!important;\n" +
                    "}\n" +
                    "a[x-apple-data-detectors] {\n" +
                    "\tcolor:inherit!important;\n" +
                    "\ttext-decoration:none!important;\n" +
                    "\tfont-size:inherit!important;\n" +
                    "\tfont-family:inherit!important;\n" +
                    "\tfont-weight:inherit!important;\n" +
                    "\tline-height:inherit!important;\n" +
                    "}\n" +
                    ".es-desk-hidden {\n" +
                    "\tdisplay:none;\n" +
                    "\tfloat:left;\n" +
                    "\toverflow:hidden;\n" +
                    "\twidth:0;\n" +
                    "\tmax-height:0;\n" +
                    "\tline-height:0;\n" +
                    "\tmso-hide:all;\n" +
                    "}\n" +
                    "@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1, h2, h3, h1 a, h2 a, h3 a { line-height:120% } h1 { font-size:30px!important; text-align:left } h2 { font-size:24px!important; text-align:left } h3 { font-size:20px!important; text-align:left } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important; text-align:left } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:24px!important; text-align:left } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important; text-align:left } .es-menu td a { font-size:14px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:14px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:14px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:14px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:18px!important; display:block!important; border-right-width:0px!important; border-left-width:0px!important; border-top-width:15px!important; border-bottom-width:15px!important } .es-adaptive table, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; max-height:inherit!important } }\n" +
                    "@media screen and (max-width:384px) {.mail-message-content { width:414px!important } }\n" +
                    "</style>\n" +
                    " </head>\n" +
                    " <body style=\"width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\n" +
                    "  <div dir=\"ltr\" class=\"es-wrapper-color\" lang=\"en\" style=\"background-color:#FFFFFF\"><!--[if gte mso 9]>\n" +
                    "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                    "\t\t\t\t<v:fill type=\"tile\" color=\"#ffffff\"></v:fill>\n" +
                    "\t\t\t</v:background>\n" +
                    "\t\t<![endif]-->\n" +
                    "   <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;background-color:#FFFFFF\">\n" +
                    "     <tr>\n" +
                    "      <td valign=\"top\" style=\"padding:0;Margin:0\">\n" +
                    "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                    "         <tr>\n" +
                    "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                    "           <table bgcolor=\"#efefef\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#EFEFEF;border-radius:20px 20px 0 0;width:600px\">\n" +
                    "             <tr>\n" +
                    "              <td align=\"left\" style=\"padding:0;Margin:0;padding-top:40px;padding-left:40px;padding-right:40px\">\n" +
                    "               <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr>\n" +
                    "                  <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:520px\">\n" +
                    "                   <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr>\n" +
                    "                      <td align=\"left\" class=\"es-m-txt-c\" style=\"padding:0;Margin:0;font-size:0px\"><a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#2D3142;font-size:18px\"><img src=\"https://ecmnnre.stripocdn.email/content/guids/CABINET_ee77850a5a9f3068d9355050e69c76d26d58c3ea2927fa145f0d7a894e624758/images/group_4076323.png\" alt=\"Confirm email\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:100px\" width=\"100\" title=\"Confirm email\"></a></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "             <tr>\n" +
                    "              <td align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:40px;padding-right:40px\">\n" +
                    "               <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr>\n" +
                    "                  <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:520px\">\n" +
                    "                   <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" bgcolor=\"#fafafa\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#fafafa;border-radius:10px\">\n" +
                    "                     <tr>\n" +
                    "                      <td align=\"left\" style=\"padding:20px;Margin:0\"><h3 style=\"Margin:0;line-height:34px;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;font-size:28px;font-style:normal;font-weight:bold;color:#2D3142\">Training Scheduling Notification System</h3><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;line-height:27px;color:#2D3142;font-size:18px\"><br></p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;line-height:27px;color:#2D3142;font-size:18px\">" +
                    "" +messageToSend +
                    " </p></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "           </table></td>\n" +
                    "         </tr>\n" +
                    "       </table>\n" +
                    "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                    "         <tr>\n" +
                    "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                    "           <table bgcolor=\"#efefef\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#EFEFEF;width:600px\">\n" +
                    "             <tr>\n" +
                    "              <td align=\"left\" style=\"padding:0;Margin:0;padding-left:40px;padding-right:40px\">\n" +
                    "               <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                 <tr>\n" +
                    "                  <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:520px\">\n" +
                    "                   <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                     <tr>\n" +
                    "                      <td align=\"left\" style=\"padding:0;Margin:0;padding-top:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;line-height:27px;color:#2D3142;font-size:18px\">Thanks,&nbsp;</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;line-height:27px;color:#2D3142;font-size:18px\">Team Training Scheduling</p></td>\n" +
                    "                     </tr>\n" +
                    "                     <tr>\n" +
                    "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-bottom:20px;padding-top:40px;font-size:0\">\n" +
                    "                       <table border=\"0\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                         <tr>\n" +
                    "                          <td style=\"padding:0;Margin:0;border-bottom:1px solid #666666;background:unset;height:1px;width:100%;margin:0px\"></td>\n" +
                    "                         </tr>\n" +
                    "                       </table></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "           </table></td>\n" +
                    "         </tr>\n" +
                    "       </table>\n" +
                    "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                    "         <tr>\n" +
                    "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                    "           <table bgcolor=\"#efefef\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#EFEFEF;border-radius:0 0 20px 20px;width:600px\">\n" +
                    "             <tr>\n" +
                    "              <td class=\"esdev-adapt-off\" align=\"left\" style=\"Margin:0;padding-top:20px;padding-bottom:20px;padding-left:40px;padding-right:40px\">\n" +
                    "               <table cellpadding=\"0\" cellspacing=\"0\" class=\"esdev-mso-table\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;width:520px\">\n" +
                    "                 <tr>\n" +
                    "                  <td class=\"esdev-mso-td\" valign=\"top\" style=\"padding:0;Margin:0\">\n" +
                    "                   <table cellpadding=\"0\" cellspacing=\"0\" align=\"left\" class=\"es-left\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:left\">\n" +
                    "                     <tr>\n" +
                    "                      <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:47px\">\n" +
                    "                       <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                         <tr>\n" +
                    "                          <td align=\"center\" class=\"es-m-txt-l\" style=\"padding:0;Margin:0;font-size:0px\"><img src=\"https://ecmnnre.stripocdn.email/content/guids/CABINET_ee77850a5a9f3068d9355050e69c76d26d58c3ea2927fa145f0d7a894e624758/images/group_4076325.png\" alt=\"Demo\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;font-size:12px\" width=\"47\" title=\"Demo\"></td>\n" +
                    "                         </tr>\n" +
                    "                       </table></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                  <td style=\"padding:0;Margin:0;width:20px\"></td>\n" +
                    "                  <td class=\"esdev-mso-td\" valign=\"top\" style=\"padding:0;Margin:0\">\n" +
                    "                   <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-right\" align=\"right\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:right\">\n" +
                    "                     <tr>\n" +
                    "                      <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:453px\">\n" +
                    "                       <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                    "                         <tr>\n" +
                    "                          <td align=\"left\" style=\"padding:0;Margin:0;padding-top:10px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;line-height:24px;color:#2D3142;font-size:16px\">This link expire in 24 hours!</p></td>\n" +
                    "                         </tr>\n" +
                    "                       </table></td>\n" +
                    "                     </tr>\n" +
                    "                   </table></td>\n" +
                    "                 </tr>\n" +
                    "               </table></td>\n" +
                    "             </tr>\n" +
                    "           </table></td>\n" +
                    "         </tr>\n" +
                    "       </table></td>\n" +
                    "     </tr>\n" +
                    "   </table>\n" +
                    "  </div>\n" +
                    " </body>\n" +
                    "</html>";




            mimeMessageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Problem with sending HTML email", e);
        }

    }
}
