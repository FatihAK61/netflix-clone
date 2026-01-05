import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: false,
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {
  landingForm!: FormGroup;
  year = new Date().getFullYear();

  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {
    this.landingForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['']
    });
  }

  login() {
    this.router.navigate(['/login']);
  }

  getStarted() {
    this.router.navigate(['/signup'], {
      queryParams: {
        email: this.landingForm.value.email
      }
    });
  }

  reasons = [
    {
      title: 'Enjoy the latest movies and TV shows',
      text: 'Watch anywhere. Cancel anytime.',
      icon: 'ondemand_video'
    },
    {
      title: 'Watch everywhere',
      text: 'Stream unlimited on any device.',
      icon: 'smart_screen'
    },
    {
      title: 'Create profiles for friends and family.',
      text: 'Watch together anytime, anywhere.',
      icon: 'group_add'
    },
    {
      title: 'Download and go',
      text: 'Save your favorites easily and always have something to watch.',
      icon: 'download_for_offline'
    }
  ];

  faqs = [
    {
      question: 'How do I cancel?',
      answer: 'Click on the cancel button on the account page.'
    },
    {
      question: 'What if I change my mind?',
      answer: 'You can always cancel your account at any time.'
    },
    {
      question: 'What can I watch on Netflix?',
      answer: 'Netflix has an extensive library of feature films, documentaries, TV shows, anime, award-winning Netflix originals, and more.'
    },
    {
      question: 'Where can I watch?',
      answer: 'Watch anywhere, anytime. Sign in with your Netflix account to watch instantly on the web at netflix.com from your personal computer or on any internet-connected device that offers the Netflix app, including smart TVs, smartphones, tablets, streaming media players and game consoles.'
    },
    {
      question: 'How do I renew?',
      answer: 'Click here to renew your Netflix account now.'
    },
    {
      question: 'What can I watch on Netflix?',
      answer: 'Netflix has an extensive library of feature films, documentaries, TV shows, anime, award-winning Netflix originals, and more.'
    }
  ];
}
