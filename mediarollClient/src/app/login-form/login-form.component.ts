import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  model: any = {};
  constructor(private router: Router) { }

  ngOnInit() {
  }

  login(){
    console.log(this.model);
  }

}
