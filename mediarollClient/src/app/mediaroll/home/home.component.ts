import { Component, OnInit } from '@angular/core';

// const URL = '/api/';
const URL = 'http://localhost:8080/medias';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
  ) { }

  ngOnInit() {  }

}
