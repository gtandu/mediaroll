import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { AuthentificationService } from "../services/authentification.service";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  model: any = {};
  errorMsg: String;
  loader: boolean = false;
  constructor(private router: Router, private authService: AuthentificationService) { }

  ngOnInit() {
    this.authService.logout();
    $(document).ready(function(){
      $('.parallax').parallax();
    });
  }

  login(){
    console.log(this.model);

    this.authService.login(this.model.email, this.model.password).subscribe(
      result => {
        if (result === true) {
          // login successful
          console.log("TOKEN OK REDIRECT APP");
          this.loader = true;
          //this.router.navigate(['/userspace/librairies']);
        } else {
          // login failed
          console.log("ERROR LOGIN");
        }
      },
      error => {
        if(error.status === 401){
          this.errorMsg = "L'email ou le mot de passe est incorrecte !"
        }
        else{
          this.errorMsg = "Une erreur technique est survenue !"
        }
      });
  }

}
